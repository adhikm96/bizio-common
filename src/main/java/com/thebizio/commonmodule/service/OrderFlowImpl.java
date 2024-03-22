package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import com.thebizio.commonmodule.dto.*;
import com.thebizio.commonmodule.dto.lead.LeadRegistrationDto;
import com.thebizio.commonmodule.dto.tax.TaxAddress;
import com.thebizio.commonmodule.dto.tax.TaxResp;
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.*;
import com.thebizio.commonmodule.exception.*;
import com.thebizio.commonmodule.service.tax.ITaxService;
import com.thebizio.commonmodule.service.tax.TaxJarService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class OrderFlowImpl implements IOrderFlow {

    Logger logger = LoggerFactory.getLogger(OrderFlowImpl.class);

    private final PromotionService promotionService;

    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;

    private final BillingAccountService billingAccountService;

    private final OrderPayloadService orderPayloadService;

    private final ITaxService taxJarService;

    public OrderFlowImpl(PromotionService promotionService, EntityManager entityManager, ObjectMapper objectMapper, ModelMapper modelMapper, BillingAccountService billingAccountService, OrderPayloadService orderPayloadService, TaxJarService taxJarService) {
        this.promotionService = promotionService;
        this.entityManager = entityManager;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.billingAccountService = billingAccountService;
        this.orderPayloadService = orderPayloadService;
        this.taxJarService = taxJarService;
    }

    @Override
    public String createCustomer(String name, String email) {
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", name);
        customerMap.put("email", email);
        try {
            return Customer.create(customerMap).getId();
        } catch (StripeException e) {
            logger.error(e.getMessage());
            throw new ServerException("try again later or contact support");
        }
    }

    private boolean nullCheckpoint(JsonNode jsonNode, String property) {
        if (jsonNode.has(property)) {
            if (jsonNode.get(property) == null) return true;
            if (jsonNode.get(property).asText().isEmpty()) return true;
            if (jsonNode.get(property).asText().equals("null")) return true;
            else return false;
        } else return true;
    }

    @Override
    public void createOrderPayload(Order order, String payloadType, String payload, String stripeCustomerId) {
        OrderPayload orderPayload = new OrderPayload();
        orderPayload.setOrder(order);
        orderPayload.setPayloadType(payloadType);
        orderPayload.setPayload(payload);
        orderPayload.setStripeCustomerId(stripeCustomerId);
        entityManager.persist(orderPayload);
    }

    @Transactional
    @Override
    public Order createOrder(String orgCode, ProductVariant productVariant, Price price,
            Promotion promotion, BillingAddress billingAddress
    ) throws JsonProcessingException {
        Order order = new Order();
        order.setPostingDate(LocalDate.now());

        if (!price.getProductVariant().getId().equals(productVariant.getId()))
            throw new ValidationException("priceId don't belong to variantId");

        // check subscription already exists
        // expire old orders

        // check promotion is valid

        boolean isPromotion = promotion != null;

        if (isPromotion && !promotion.isValid()) throw new ValidationException("promotion is not valid");
        boolean isFullDiscount = false;
        if (isPromotion) order.setPromotion(promotion);

        Double discount = null;
        Double amount = price.getPrice();
        order.setPrice(price);

        //add addons price to product variant price
        Double grossTotal = amount;
        if (productVariant.getAddOns().size() > 0) {
            for (ProductVariant pv : productVariant.getAddOns()) {
                grossTotal += pv.getPriceRecord().getPrice();
            }
        }
        order.setGrossTotal(BigDecimal.valueOf(CalculateUtilService.roundTwoDigits(grossTotal)));

        // apply discount
        if (isPromotion) {
            Coupon coupon = promotion.getCoupon();
            if (coupon.getType().equals(CouponType.AMOUNT)) {
                discount = (double) coupon.getValue();
                if (coupon.getValue() >= CalculateUtilService.roundTwoDigits(grossTotal)) {
                    isFullDiscount = true;
                }
            } else {
                discount = coupon.getValue() * (CalculateUtilService.roundTwoDigits(grossTotal)) / 100;
                if (coupon.getValue() == 100d) {
                    isFullDiscount = true;
                }
            }

            discount = CalculateUtilService.roundTwoDigits(discount);
            promotionService.incrementPromocodeCounter(promotion);
        }

        if (discount != null) {
            order.setDiscount(BigDecimal.valueOf(discount));
            order.setDiscountStr("{\"" + promotion.getCode() + "\":" + discount + "}");
        }

        BigDecimal tax = BigDecimal.ZERO;

        if (!isFullDiscount) {

            // get taxes for given address
            // call taxjar get taxes

            TaxResp taxResp;
            try {
                taxResp = taxJarService.calculateTax(billingAddress, BigDecimal.valueOf(grossTotal), discount == null ? BigDecimal.ZERO : BigDecimal.valueOf(discount));
            } catch (TaxCalculationException | JsonProcessingException e) {
                logger.error(e.getMessage());
                throw new ValidationException("some error occurred while creating order");
            }

            tax = CalculateUtilService.nullOrZeroValue(taxResp.getTax(), BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN);
            order.setTax(tax);
            order.setTaxStr(objectMapper.writeValueAsString(taxResp.getSummary()));
        }

        if (discount != null) {
            order.setNetTotal(BigDecimal.valueOf(CalculateUtilService.roundTwoDigits(grossTotal)).add(tax).subtract(BigDecimal.valueOf(discount)));
        } else {
            order.setNetTotal(BigDecimal.valueOf(CalculateUtilService.roundTwoDigits(grossTotal)).add(tax));
        }

        if (order.getNetTotal().compareTo(BigDecimal.ZERO) < 0) {
            order.setNetTotal(BigDecimal.ZERO);
        }

        if (promotion != null) {
            order.setPromoCode(promotion.getCode());
        }
        order.setProductVariant(productVariant);
        order.setProduct(productVariant.getProduct());
        order.setStatus(OrderStatus.IN_PROGRESS);

        entityManager.persist(order);
        return order;
    }

    @Transactional
    @Override
    public Order createOrder(ProductVariant productVariant, Price price,
            Promotion promotion, Lead lead) throws JsonProcessingException {

        Order order = createOrder(null, productVariant, price, promotion,  modelMapper.map(lead, BillingAddress.class));
        order.setLead(lead);
        entityManager.persist(order);
        return order;
    }

    @Override
    public String checkout(String stripeCustId) {
        SetupIntentCreateParams params = SetupIntentCreateParams
                .builder()
                .setCustomer(stripeCustId)
                .addPaymentMethodType("card")
                .build();
        try {
            return SetupIntent.create(params).getClientSecret();
        } catch (StripeException e) {
            logger.error(e.getMessage());
            throw new ValidationException("try again later or contact support");
        }
    }

    @Override
    public String checkout(@Valid CheckoutReqDto dto) {
        assert dto.getStripeCustomerId() != null;

        SetupIntentCreateParams.Builder builder = SetupIntentCreateParams
                .builder()
                .setCustomer(dto.getStripeCustomerId());

        if (dto.getPaymentMethods() == null || dto.getPaymentMethods().size() == 0)
            builder.addPaymentMethodType("card");
        else
            builder.addAllPaymentMethodType(dto.getPaymentMethods().stream().map(e -> e.toString().toLowerCase()).collect(Collectors.toList()));

        builder.putMetadata("primaryAccount", String.valueOf(dto.isPrimaryAccount()));
        builder.putMetadata("doCreate", String.valueOf(dto.isDoCreate()));
        if (dto.getOrderRefNo() != null) builder.putMetadata("orderRefNo", String.valueOf(dto.getOrderRefNo()));
        SetupIntentCreateParams params = builder.build();

        try {
            return SetupIntent.create(params).getClientSecret();
        } catch (StripeException e) {
            logger.error(e.getMessage());
            throw new ValidationException("try again later or contact support");
        }
    }

    public OrderResponseDto createOrderResponse(Order order, String stripeCustId, String clientSecretKey) throws JsonProcessingException {
        OrderResponseDto dto = new OrderResponseDto();
        ProductVariant pv = order.getProductVariant();
        dto.setProductName(pv.getProduct().getName());
        dto.setProductCode(pv.getProduct().getCode());
        dto.setAttributeValue(pv.getVariantAttributeValue());
        dto.setPlanType(order.getProductVariant().getPlanType());

        dto.setPrice(BigDecimal.valueOf(order.getPrice().getPrice()));
        dto.setGrossTotal(order.getGrossTotal());

        dto.setTax(order.getTax());
        if (order.getTaxStr() != null && !order.getTaxStr().equals("[]")) {
            dto.setTaxStr(objectMapper.readTree(order.getTaxStr()));
            dto.setTaxPercentage(CalculateUtilService.calculateTaxPercentage(dto.getTaxStr()) + "%");
        } else {
            dto.setTaxPercentage("0%");
        }


        dto.setDiscount(order.getDiscount());
        if (order.getDiscountStr() != null) dto.setDiscountStr(objectMapper.readTree(order.getDiscountStr()));
        dto.setNetTotal(order.getNetTotal());

        List<AddOnsDto> addons = new ArrayList<>();
        for (ProductVariant addOn : pv.getAddOns()) {
            AddOnsDto addOnDto = new AddOnsDto();
            addOnDto.setName(addOn.getName());
            addOnDto.setPrice(BigDecimal.valueOf(addOn.getPriceRecord().getPrice()));
            addons.add(addOnDto);
        }
        dto.setAddons(addons);
        dto.setOrderRefNo(order.getRefNo());
        dto.setStripeCustomerId(stripeCustId);
        dto.setClientSecretKey(clientSecretKey);
        return dto;
    }

    public Account createAccountFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Account acc = new Account();
        acc.setStatus(Status.ENABLED);
        acc.setType(AccType.valueOf(jsonNode.get("accountType").asText()));
        return acc;
    }

    @Override
    public Organization createOrganizationFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Organization org = new Organization();

        org.setName(!nullCheckpoint(jsonNode, "orgName") ? jsonNode.get("orgName").asText() : "main");
        org.setBillingEmail(jsonNode.get("signupEmail").asText().toLowerCase());
        if(!nullCheckpoint(jsonNode, "typeOfBusiness")) org.setTypeOfBusiness(jsonNode.get("typeOfBusiness").asText());
        if(!nullCheckpoint(jsonNode, "taxId")) org.setTaxId(jsonNode.get("taxId").asText());
        org.setStatus(Status.ENABLED);

        return org;
    }

    @Override
    public Address createAddressFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Address address = new Address();
        if (jsonNode.has("addressLine1")) address.setAddressLine1(jsonNode.get("addressLine1").asText());
        if (!nullCheckpoint(jsonNode, "addressLine2")) address.setAddressLine2(jsonNode.get("addressLine2").asText());
        address.setStatus(Status.ENABLED);
        address.setCity(jsonNode.get("city").asText());
        address.setState(jsonNode.get("state").asText());
        address.setCountry(jsonNode.get("country").asText());
        address.setZipcode(jsonNode.get("zipcode").asText());
        //attach org later
        return address;
    }

    @Override
    public void submitTax(Order order, String orgCode) throws TaxSubmissionException {
        BillingAddress ba = modelMapper.map(order.getAddress(),BillingAddress.class);
        ProductVariant pv = order.getProductVariant();
        taxJarService.submitTax(ba, order.getGrossTotal(), order.getDiscount(), order.getTax(), order.getRefNo(), pv.getCode(), pv.getId().toString(), orgCode);
    }

    @Override
    public void submitTax(ProductVariant pv, String orgCode, Address address, BigDecimal grossTotal, BigDecimal tax, String invoiceRef, BigDecimal discount) throws TaxSubmissionException {
        BillingAddress ba = modelMapper.map(address,BillingAddress.class);
        taxJarService.submitTax(ba, grossTotal, discount, tax, invoiceRef, pv.getCode(), pv.getId().toString(), orgCode);
    }

    @Override
    public Payment createPayment(BigDecimal amount, BillingAccount ba, PaymentStatus status, String paymentRef) {
        Payment pay = new Payment();
        pay.setPaymentDate(LocalDate.now());
        pay.setAmount(amount);
        pay.setBillingAccount(ba);
        pay.setStatus(status);
        pay.setPaymentRef(paymentRef);
        entityManager.persist(pay);
        return pay;
    }

    @Override
    public Invoice createInvoiceFromOrder(Order order, Subscription sub, InvoiceStatus status, Payment payment) {
        Invoice inv = new Invoice();
        inv.setPrice(order.getPrice());
        inv.setProduct(order.getProduct());
        inv.setProductVariant(order.getProductVariant());
        inv.setGrossTotal(order.getGrossTotal());
        inv.setNetTotal(order.getNetTotal());
        if (order.getDiscount() != null) inv.setDiscount(order.getDiscount());
        if (order.getDiscountStr() != null) inv.setDiscountStr(order.getDiscountStr());
        if (order.getTax() != null) inv.setTax(order.getTax());
        if (order.getTaxStr() != null) inv.setTaxStr(order.getTaxStr());
        inv.setAddress(order.getAddress());
        inv.setPostingDate(LocalDate.now());
        if (order.getPromoCode() != null) inv.setPromoCode(order.getPromoCode());
        inv.setSubscription(sub);
        inv.setStatus(status);
        inv.setPayment(payment);

        entityManager.persist(inv);

        return inv;
    }

    @Override
    public Contact createContactFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Contact contact = new Contact();
        if (!nullCheckpoint(jsonNode, "firstName")) contact.setFirstName(jsonNode.get("firstName").asText());
        if (!nullCheckpoint(jsonNode, "middleName")) contact.setMiddleName(jsonNode.get("middleName").asText());
        if (!nullCheckpoint(jsonNode, "lastName")) contact.setLastName(jsonNode.get("lastName").asText());
        if (!nullCheckpoint(jsonNode, "email")) contact.setEmail(jsonNode.get("email").asText());
        if (!nullCheckpoint(jsonNode, "mobile")) contact.setMobile(jsonNode.get("mobile").asText());
        if (!nullCheckpoint(jsonNode, "fax")) contact.setFax(jsonNode.get("fax").asText());
        if (!nullCheckpoint(jsonNode, "website")) contact.setWebsite(jsonNode.get("website").asText());
        contact.setStatus(Status.ENABLED);

        //attach org later
        return contact;
    }

    @Override
    public TemporaryOrderResponseDto temporaryOrderResponse(@NotNull ProductVariant productVariant, @NotNull Price price, BillingAddress billingAddress) throws JsonProcessingException {
        TemporaryOrderResponseDto dto = new TemporaryOrderResponseDto();
        dto.setProductName(productVariant.getProduct().getName());
        dto.setProductCode(productVariant.getProduct().getCode());
        dto.setAttributeValue(productVariant.getVariantAttributeValue());
        dto.setPlanType(productVariant.getPlanType());

        dto.setPrice(BigDecimal.valueOf(price.getPrice()));

        Double grossTotal = price.getPrice();
        if (productVariant.getAddOns().size() > 0) {
            for (ProductVariant pv : productVariant.getAddOns()) {
                grossTotal += pv.getPriceRecord().getPrice();
            }
        }

        dto.setGrossTotal(BigDecimal.valueOf(grossTotal));

        BigDecimal tax = BigDecimal.ZERO;

        TaxResp taxResp;
        try {
            taxResp = taxJarService.calculateTax(billingAddress, BigDecimal.valueOf(grossTotal), BigDecimal.ZERO);
        } catch (TaxCalculationException | JsonProcessingException e) {
            logger.error(e.getMessage());
            throw new ValidationException("some error occurred while creating order");
        }

        tax = CalculateUtilService.nullOrZeroValue(taxResp.getTax(), BigDecimal.ZERO).setScale(2, RoundingMode.HALF_EVEN);
        dto.setTax(tax);

        if (dto.getTaxStr() != null && !dto.getTaxStr().equals("[]")) {
            dto.setTaxStr(objectMapper.readTree(taxResp.getSummary()));
            dto.setTaxPercentage(CalculateUtilService.calculateTaxPercentage(dto.getTaxStr()) + "%");
        } else {
            dto.setTaxPercentage("0%");
        }

        dto.setDiscount(BigDecimal.valueOf(0));
        dto.setNetTotal(BigDecimal.valueOf(CalculateUtilService.roundTwoDigits(grossTotal)).add(tax));

        List<AddOnsDto> addons = new ArrayList<>();
        for (ProductVariant addOn : productVariant.getAddOns()) {
            AddOnsDto addOnDto = new AddOnsDto();
            addOnDto.setName(addOn.getName());
            addOnDto.setPrice(BigDecimal.valueOf(addOn.getPriceRecord().getPrice()));
            addons.add(addOnDto);
        }
        dto.setAddons(addons);
        return dto;
    }

    @Override
    public Lead createLead(LeadRegistrationDto dto) {
        Lead lead = new Lead();
        lead.setAccType(dto.getAccountType());
        lead.setStatus(LeadStatus.OPEN);

        lead.setFirstName(dto.getPersonalDetails().getFirstName());
        lead.setLastName(dto.getPersonalDetails().getLastName());
        lead.setDob(dto.getPersonalDetails().getDob());
        lead.setGender(dto.getPersonalDetails().getGender());
        lead.setJobTitle(dto.getPersonalDetails().getJobTitle());
        lead.setWorkEmail(dto.getPersonalDetails().getWorkEmail().toLowerCase());
        lead.setPhoneNumber(dto.getPersonalDetails().getPhoneNumber());

        lead.setAddressLine1(dto.getAddress().getAddressLine1());
        lead.setAddressLine2(dto.getAddress().getAddressLine2());
        lead.setCity(dto.getAddress().getCity());
        lead.setState(dto.getAddress().getState());
        lead.setCountry(dto.getAddress().getCountry());
        lead.setZipcode(dto.getAddress().getZipcode());

        lead.setSignupEmail(dto.getContact().getSignupEmail().toLowerCase());
        lead.setMobile(dto.getContact().getMobile());

        if (dto.getOrganizationDetails() != null){
            lead.setOrgName(dto.getOrganizationDetails().getName());
            lead.setWebsite(dto.getOrganizationDetails().getWebsite());
            lead.setTaxId(dto.getOrganizationDetails().getTaxId());
            lead.setTypeOfBusiness(dto.getOrganizationDetails().getTypeOfBusiness());
            lead.setEmailDomain(dto.getOrganizationDetails().getEmailDomain());
        }
        lead.setStayInformedAboutBizio(dto.isStayInformedAboutBizio());
        lead.setTermsConditionsAgreed(dto.isTermsConditionsAgreed());
        entityManager.persist(lead);

        return lead;
    }


    @Override
    public Subscription createSubscription(Order order, Organization organization, User user) {
        Subscription sub = new Subscription();
        ProductVariant pv = order.getProductVariant();
        assert pv != null;
        sub.setRenewNextSubscription(true);

        if (pv.getVariantAttributeValue().equals("YEARLY")) {
            sub.setValidFrom(LocalDate.now());
            sub.setValidTill(LocalDate.now().plusYears(1));
            sub.setSubscriptionType(SubscriptionTypeEnum.YEARLY);
            sub.setNextRenewalDate(LocalDate.now().plusYears(1).plusDays(1));
        } else {
            sub.setValidFrom(LocalDate.now());
            sub.setValidTill(LocalDate.now().plusMonths(1));
            sub.setSubscriptionType(SubscriptionTypeEnum.MONTHLY);
            sub.setNextRenewalDate(LocalDate.now().plusMonths(1).plusDays(1));
        }
        sub.setSubscriptionStatus(SubscriptionStatusEnum.ACTIVE);
        sub.setOrg(organization);
        List<Application> applications = new ArrayList<>();
        for (BundleItem bi : pv.getBundleItems()) {
            applications.add(bi.getBundleItem().getApplication());
        }
        sub.setApplications(applications);

        sub.setOrder(order);
        sub.setProduct(order.getProduct());
        sub.setProductVariant(pv);
        sub.setExtension(false);
        sub.setPrice(order.getPrice());
        sub.setPromotion(order.getPromotion());
        sub.setOccupiedSeats(0);
        if (order.getPromoCode() != null) sub.setPromotion(order.getPromotion());
        entityManager.persist(sub);

        if (user != null) {
            SubscriptionUser subscriptionUser = new SubscriptionUser();
            subscriptionUser.setSubscription(sub);
            subscriptionUser.setUser(user);
            entityManager.persist(subscriptionUser);

            // occupy seats from sub
            sub.setOccupiedSeats(1);
            //primary user added to subscription
            sub.setPrimaryUser(user);
            entityManager.persist(sub);
        }

        return sub;
    }

    @Override
    public BillingAccount createBillingAccount(PaymentIntent paymentIntent, Organization organization, Boolean primaryAccount) {
        List<BillingAccount> billingAccount = billingAccountService.getBillingAccountByStripeId(paymentIntent.getPaymentMethod());
        if (billingAccount.size() >= 1) {
            if (billingAccount.get(0).getOrganization() == null) {
                billingAccount.get(0).setOrganization(organization);
                entityManager.persist(billingAccount);
            }
            return billingAccount.get(0);
        } else {
            PaymentMethod stripePM = fetchPaymentMethod(paymentIntent.getPaymentMethod());
            BillingAccount ba = new BillingAccount();
            if (stripePM.getType().equals("card")) {
                ba.setStripePaymentMethodId(stripePM.getId());
                ba.setCardBrand(stripePM.getCard().getBrand());
                ba.setExpMonth(stripePM.getCard().getExpMonth().toString());
                ba.setExpYear(stripePM.getCard().getExpYear().toString());
                ba.setFingerprint(stripePM.getCard().getFingerprint());
                ba.setLast4(stripePM.getCard().getLast4());
                ba.setBillingAccType(BillingAccType.CARD);
                ba.setAccHolderName(stripePM.getBillingDetails().getName());
                ba.setOrganization(organization);
                ba.setStatus(Status.ENABLED);
                ba.setPrimaryAccount(primaryAccount);
                ba.setAddress(mapAddress(stripePM.getBillingDetails().getAddress()));
                entityManager.persist(ba);
            } else if (stripePM.getType().equals("us_bank_account")) {
                ba.setStripePaymentMethodId(stripePM.getId());
                ba.setBillingAccType(BillingAccType.BANK);
                ba.setAccHolderType(stripePM.getUsBankAccount().getAccountHolderType());
                ba.setAccHolderName(stripePM.getBillingDetails().getName());
                ba.setPgAccType(stripePM.getUsBankAccount().getAccountType());
                ba.setFinancialConnectionsAcc(stripePM.getUsBankAccount().getFinancialConnectionsAccount());
                ba.setFingerprint(stripePM.getUsBankAccount().getFingerprint());
                ba.setLast4(stripePM.getUsBankAccount().getLast4());
                ba.setBankRoutingNumber(stripePM.getUsBankAccount().getRoutingNumber());
                ba.setBankName(stripePM.getUsBankAccount().getBankName());
                ba.setOrganization(organization);
                ba.setStatus(Status.ENABLED);
                ba.setPrimaryAccount(primaryAccount);
                entityManager.persist(ba);
            }
            return ba;
        }
    }

    private Address mapAddress(com.stripe.model.Address address) {
        Address add = new com.thebizio.commonmodule.entity.Address();
        add.setAddressLine1(address.getLine1());
        add.setAddressLine2(address.getLine2());
        add.setCity(address.getCity());
        add.setState(address.getState());
        add.setZipcode(address.getPostalCode());
        add.setCountry(address.getCountry());
        add.setStatus(Status.ENABLED);
        entityManager.persist(add);
        return add;
    }

    private PaymentMethod fetchPaymentMethod(String id) {
        try {
            PaymentMethod pm = PaymentMethod.retrieve(id);
            return pm;
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public BillingAccType fetchBillingAccTypeFromPmId(String pmId) {
        try {
            return PaymentMethod.retrieve(pmId).getType().equals("card") ? BillingAccType.CARD : BillingAccType.BANK;
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void createContactFromLeadForUser(Lead lead, User user){
        Contact userContact = new Contact();
        userContact.setFirstName(lead.getFirstName());
        userContact.setLastName(lead.getLastName());
        userContact.setEmail(lead.getWorkEmail());
        userContact.setMobile(lead.getPhoneNumber());
        userContact.setPrimaryContact(true);
        userContact.setStatus(Status.ENABLED);
        entityManager.persist(userContact);

        user.setContact(userContact);
        entityManager.persist(user);
    }

    @Override
    public void createOrgDomain(Organization org, String domain, DomainStatus status){
        OrgDomain orgDomain = new OrgDomain();
        orgDomain.setOrganization(org);
        orgDomain.setDomain(domain);
        orgDomain.setStatus(status);
        entityManager.persist(orgDomain);
    }

    @Override
    public PostpaidAccountResponse setUpAccountForPostpaidVariant(String orderRefNo, String paymentMethodId, BillingAccount billingAccount) throws JsonProcessingException {
        OrderPayload op = orderPayloadService.findByOrderRefNo(orderRefNo);
        if (op == null) throw new ValidationException("order not found");
        Order order = op.getOrder();
        Lead lead = order.getLead();
        String leadString = lead == null ? null : objectMapper.writeValueAsString(lead);
        JsonNode checkoutDto = objectMapper.readTree(op.getPayload());
        Organization parentOrg = order.getParentOrganization();

        //create org
        Organization org = leadString != null ? createOrganizationFromPayload(leadString) : createOrganizationFromPayload(op.getPayload());
        org.setParent(parentOrg);
        org.setStripeCustomerId(op.getStripeCustomerId());
        if (parentOrg != null) org.setAccount(parentOrg.getAccount());
        entityManager.persist(org);

        createOrgDomain(org, lead != null ? lead.getEmailDomain() : checkoutDto.get("emailDomain").asText(), DomainStatus.PENDING);
        //create address
        Address address = lead != null ? createAddressFromPayload(leadString) : createAddressFromPayload(op.getPayload());
        address.setOrg(org);
        address.setPrimaryAddress(true);
        entityManager.persist(address);

        //create contact for child org
        if (lead == null){
            Contact contact = createContactFromPayload(op.getPayload());
            contact.setOrg(org);
            contact.setPrimaryContact(true);
            entityManager.persist(contact);
        }

        PostpaidAccountResponse response = new PostpaidAccountResponse();

        User user = null;
        if (lead != null) {
            //create account
            Account account = createAccountFromPayload(op.getPayload());
            account.setSignupEmail(lead.getSignupEmail().toLowerCase());
            account.setEmail(lead.getAccType().equals(AccType.INDIVIDUAL) ? checkoutDto.get("email").asText().toLowerCase() : lead.getSignupEmail().toLowerCase());
            account.setPrimaryOrganization(org);
            entityManager.persist(account);

            //set account in org
            org.setAccount(account);
            entityManager.persist(org);

            //create user
            user = new User();
            user.setFirstName(lead.getFirstName());
            user.setLastName(lead.getLastName());
            user.setUsername(checkoutDto.get("userName").asText().toLowerCase());
            if (lead.getAccType().equals(AccType.INDIVIDUAL)){
                user.setEmail(checkoutDto.get("email").asText().toLowerCase());
            }else {
                user.setEmail(lead.getWorkEmail().toLowerCase());
                user.setJobTitle(lead.getJobTitle());
            }
            user.setOrganization(org);
            user.setLastPasswordChangeDate(LocalDateTime.now());
            user.setLastEmailChangeDate(LocalDateTime.now());
            user.setStayInformedAboutBizio(lead.isStayInformedAboutBizio());
            user.setTermsConditionsAgreed(lead.isTermsConditionsAgreed());
            if (user.getTermsConditionsAgreed()) user.setTermsConditionsAgreedTimestamp(LocalDateTime.now());
            user.setStatus(Status.ENABLED);
            user.setGender(lead.getGender());
            user.setDob(lead.getDob());
            entityManager.persist(user);

            response.setUser(user);

            //set account in address
            address.setAccount(account);
            entityManager.persist(address);

            //create contact for primary user or org acc
            if (lead.getAccType().equals(AccType.ORGANIZATION)) {
                createContactFromLeadForUser(lead, user);
            }

            PaymentIntent pi = new PaymentIntent();
            pi.setPaymentMethod(paymentMethodId);

            createBillingAccount(pi, org, true);
        }

        Subscription sub = createSubscription(order, org, user);
        if (order.getProductVariant().getVariantAttributeValue().equals("MONTHLY")) {
            sub.setValidFrom(LocalDate.now());
            sub.setValidTill(LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()));
            sub.setNextRenewalDate(sub.getValidTill().plusDays(1));
        }
        sub.setPreferredBillingAccount(billingAccount);
        entityManager.persist(sub);

        order.setAddress(address);
        order.setOrganization(org);
        order.setStatus(OrderStatus.COMPLETED);
        entityManager.persist(order);

        response.setOrder(order);
        response.setOrganization(org);
        return response;
    }

    @Override
    public TaxAddress validateBillingAddress(BillingAddress address) throws InvalidAddressException {
        return taxJarService.getAddress(address);
    }

    @Override
    public void validateBillingAccountExpiry(BillingAccount billingAccount) {
        if (billingAccount.getBillingAccType().equals(BillingAccType.CARD)) {
            if (Long.parseLong(billingAccount.getExpYear()) < LocalDate.now().getYear())
                throw new ValidationException("card has been expired");
            if (Long.parseLong(billingAccount.getExpYear()) == LocalDate.now().getYear()) {
                if (Long.parseLong(billingAccount.getExpMonth()) < LocalDate.now().getMonthValue())
                    throw new ValidationException("card has been expired");
            }
        }
    }

    @Override
    public PaymentIntent payment(String stripePaymentMethodId, String stripeCustomerId, Long amount, String orderRefNo) {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setCurrency("usd")
                        .setAmount(amount)
                        .putMetadata("order_ref", orderRefNo)
                        .setPaymentMethod(stripePaymentMethodId)
                        .setCustomer(stripeCustomerId)
                        .setConfirm(true)
                        .setOffSession(true)
                        .build();
        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent;
        } catch (CardException e) {
            // Error code will be authentication_required if authentication is needed
            System.out.println("Error code is : " + e.getCode());
            String paymentIntentId = e.getStripeError().getPaymentIntent().getId();
            try {
                PaymentIntent paymentIntent = PaymentIntent.retrieve(paymentIntentId);
                return paymentIntent;
            } catch (StripeException ex) {
                logger.error(ex.getMessage());
                throw new ServerException("try again later or contact support");
            }
        } catch (StripeException e) {
            logger.error(e.getMessage());
            throw new ServerException("try again later or contact support");
        }
    }
}
