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
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.*;
import com.thebizio.commonmodule.exception.ServerException;
import com.thebizio.commonmodule.exception.ValidationException;
import net.avalara.avatax.rest.client.enums.DocumentType;
import net.avalara.avatax.rest.client.models.AddressResolutionModel;
import net.avalara.avatax.rest.client.models.TransactionModel;
import net.avalara.avatax.rest.client.models.TransactionSummary;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderFlowImpl implements IOrderFlow {

    Logger logger = LoggerFactory.getLogger(OrderFlowImpl.class);

    private final PromotionService promotionService;

    private final CalculateUtilService calculateUtilService;

    private final AvalaraService avalaraService;

    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;

    private final ModelMapper modelMapper;

    private final BillingAccountService billingAccountService;

    private final OrderPayloadService orderPayloadService;

    public OrderFlowImpl(PromotionService promotionService, CalculateUtilService calculateUtilService, AvalaraService avalaraService, EntityManager entityManager, ObjectMapper objectMapper, ModelMapper modelMapper,BillingAccountService billingAccountService,OrderPayloadService orderPayloadService) {
        this.promotionService = promotionService;
        this.calculateUtilService = calculateUtilService;
        this.avalaraService = avalaraService;
        this.entityManager = entityManager;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.billingAccountService = billingAccountService;
        this.orderPayloadService = orderPayloadService;
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

    private boolean nullCheckpoint(JsonNode jsonNode,String property){
        if (jsonNode.has(property)) {
            if (jsonNode.get(property) == null) return true;
            if (jsonNode.get(property).asText().isEmpty()) return true;
            if (jsonNode.get(property).asText().equals("null")) return true;
            else return false;
        }
        else return true;
    }

    @Override
    public void createOrderPayload(Order order,String payloadType,String payload,String stripeCustomerId) {
        OrderPayload orderPayload = new OrderPayload();
        orderPayload.setOrder(order);
        orderPayload.setPayloadType(payloadType);
        orderPayload.setPayload(payload);
        orderPayload.setStripeCustomerId(stripeCustomerId);
        entityManager.persist(orderPayload);
    }

    @Transactional
    @Override
    public Order createOrder(
            @NotNull String orgCode,
            @NotNull ProductVariant productVariant,
            @NotNull Price price,
            Promotion promotion,
            @Valid @NotNull BillingAddress billingAddress
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

        Double discount = null;
        Double amount = price.getPrice();
        order.setPrice(price);

        //add addons price to product variant price
        Double grossTotal = amount;
        if(productVariant.getAddOns().size() > 0) {
            for (ProductVariant pv : productVariant.getAddOns()) {
                grossTotal += pv.getDefaultPrice();
            }
        }
        order.setGrossTotal(BigDecimal.valueOf(grossTotal));

        // apply discount
        if (isPromotion) {
            Coupon coupon = promotion.getCoupon();
            if (coupon.getType().equals(CouponType.AMOUNT)) {
                discount = (double) coupon.getValue();
                if (coupon.getValue() >= grossTotal) {
                    isFullDiscount = true;
                }
            } else {
                discount = coupon.getValue() * (grossTotal) / 100;
                if (coupon.getValue() == 100d) {
                    isFullDiscount = true;
                }
            }

            discount = calculateUtilService.roundTwoDigits(discount);
            promotionService.incrementPromocodeCounter(promotion);
        }

        BigDecimal totalWithDiscount = BigDecimal.valueOf(grossTotal);;
        if (discount != null) {
            totalWithDiscount = totalWithDiscount.subtract(BigDecimal.valueOf(discount));
            order.setDiscount(BigDecimal.valueOf(discount));
            order.setDiscountStr("{\""+promotion.getCode()+"\":"+discount+"}");
        }

        BigDecimal tax = BigDecimal.ZERO;
        List<TransactionSummary> transactionSummaryList = new ArrayList<>();

        if (!isFullDiscount) {
            TransactionModel tm = null;

            // get taxes for given address
            // call avalara get taxes

            try {
                tm = avalaraService.createTransaction(
                        billingAddress,
                        productVariant,
                        orgCode,
                        DocumentType.SalesOrder,
                        totalWithDiscount
                );
            } catch (Exception e) {
                logger.error(e.getMessage());
               throw new ValidationException("some error occurred while creating order");
            }

            tax = calculateUtilService.nullOrZeroValue(tm.getTotalTax(), BigDecimal.ZERO);
            order.setTax(tax);
            order.setTaxStr(objectMapper.writeValueAsString(tm.getSummary()));
        }

        if (discount != null) {
            order.setNetTotal(BigDecimal.valueOf(grossTotal).add(tax).subtract(BigDecimal.valueOf(discount)));
        } else {
            order.setNetTotal(BigDecimal.valueOf(grossTotal).add(tax));
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
    public String checkout(@Valid  CheckoutReqDto dto) {
        assert dto.getStripeCustomerId() != null;

        SetupIntentCreateParams.Builder builder = SetupIntentCreateParams
                .builder()
                .setCustomer(dto.getStripeCustomerId());

        if(dto.getPaymentMethods() == null || dto.getPaymentMethods().size() == 0)
            builder.addPaymentMethodType("card");
        else
            builder.addAllPaymentMethodType(dto.getPaymentMethods().stream().map(e -> e.toString().toLowerCase()).collect(Collectors.toList()));

        builder.putMetadata("primaryAccount", String.valueOf(dto.isPrimaryAccount()));
        builder.putMetadata("doCreate", String.valueOf(dto.isDoCreate()));
        if (dto.getOrderRefNo() != null ) builder.putMetadata("orderRefNo", String.valueOf(dto.getOrderRefNo()));
        SetupIntentCreateParams params = builder.build();

        try {
            return SetupIntent.create(params).getClientSecret();
        } catch (StripeException e) {
            logger.error(e.getMessage());
            throw new ValidationException("try again later or contact support");
        }
    }

    public OrderResponseDto createOrderResponse(Order order,String stripeCustId, String clientSecretKey) throws JsonProcessingException {
        OrderResponseDto dto = new OrderResponseDto();
        ProductVariant pv = order.getProductVariant();
        dto.setProductName(pv.getProduct().getName());
        dto.setProductCode(pv.getProduct().getCode());
        dto.setAttributeValue(pv.getVariantAttributeValue());

        dto.setPrice(BigDecimal.valueOf(order.getPrice().getPrice()));
        dto.setGrossTotal(order.getGrossTotal());

        dto.setTax(order.getTax());

        if(order.getTaxStr() != null) dto.setTaxStr(objectMapper.readTree(order.getTaxStr()));
        dto.setDiscount(order.getDiscount());
        if(order.getDiscountStr() != null) dto.setDiscountStr(objectMapper.readTree( order.getDiscountStr()));
        dto.setNetTotal(order.getNetTotal());

        List<AddOnsDto> addons = new ArrayList<>();
        for (ProductVariant addOn: pv.getAddOns()) {
            AddOnsDto addOnDto = new AddOnsDto();
            addOnDto.setName(addOn.getName());
            addOnDto.setPrice(BigDecimal.valueOf(addOn.getDefaultPrice()));
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
        org.setName(jsonNode.get("name").asText());
        if(!nullCheckpoint(jsonNode,"shortName")) org.setShortName(jsonNode.get("shortName").asText());
        if(!nullCheckpoint(jsonNode,"otherName")) org.setOtherName(jsonNode.get("otherName").asText());
        if(!nullCheckpoint(jsonNode,"description")) org.setDescription(jsonNode.get("description").asText());
        if(!nullCheckpoint(jsonNode,"industry")) org.setIndustry(jsonNode.get("industry").asText());
        if(!nullCheckpoint(jsonNode,"className")) org.setClassName(jsonNode.get("className").asText());
        if(!nullCheckpoint(jsonNode,"structure")) org.setStructure(jsonNode.get("structure").asText());
        if(!nullCheckpoint(jsonNode,"industryType")) org.setIndustryType(jsonNode.get("industryType").asText());
        if(!nullCheckpoint(jsonNode,"exchange")) org.setExchange(jsonNode.get("exchange").asText());
        if(!nullCheckpoint(jsonNode,"market")) org.setMarket(jsonNode.get("market").asText());
        if(!nullCheckpoint(jsonNode,"symbol")) org.setSymbol(jsonNode.get("symbol").asText());
        org.setStatus(Status.ENABLED);

        //attach parent org and account later

        return org;
    }

    @Override
    public Address createAddressFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Address address = new Address();
        if(jsonNode.has("addressLine1")) address.setAddressLine1(jsonNode.get("addressLine1").asText());
        if(!nullCheckpoint(jsonNode,"addressLine2")) address.setAddressLine2(jsonNode.get("addressLine2").asText());
        address.setStatus(Status.ENABLED);
        address.setCity(jsonNode.get("city").asText());
        address.setState(jsonNode.get("state").asText());
        address.setCountry(jsonNode.get("country").asText());
        address.setZipcode(jsonNode.get("zipcode").asText());
        //attach org later
        return address;
    }

    @Override
    public void submitTaxToAvalara(Order order,String orgCode) throws Exception {
        BillingAddress ba = modelMapper.map(order.getAddress(),BillingAddress.class);
        avalaraService.createTransactionTaxInclusive(ba,order.getProductVariant(),orgCode,DocumentType.SalesInvoice,order.getNetTotal());
    }

    @Override
    public Payment createPayment(BigDecimal amount,BillingAccount ba,PaymentStatus status) {
        Payment pay = new Payment();
        pay.setPaymentDate(LocalDate.now());
        pay.setAmount(amount);
        pay.setBillingAccount(ba);
        pay.setStatus(status);
        entityManager.persist(pay);
        return pay;
    }

    @Override
    public void createInvoiceFromOrder(Order order,Subscription sub,InvoiceStatus status,Payment payment) {
        Invoice inv = new Invoice();
        inv.setPrice(order.getPrice());
        inv.setProduct(order.getProduct());
        inv.setProductVariant(order.getProductVariant());
        inv.setGrossTotal(order.getGrossTotal());
        inv.setNetTotal(order.getNetTotal());
        if (order.getDiscount() != null) inv.setDiscount(order.getDiscount());
        if (order.getDiscountStr() != null) inv.setDiscountStr(order.getDiscountStr());
        if(order.getTax() != null) inv.setTax(order.getTax());
        if(order.getTaxStr() != null) inv.setTaxStr(order.getTaxStr());
        inv.setAddress(order.getAddress());
        inv.setPostingDate(LocalDate.now());
        if(order.getPromoCode() != null) inv.setPromoCode(order.getPromoCode());
        inv.setSubscription(sub);
        inv.setStatus(status);
        inv.setPayment(payment);

        entityManager.persist(inv);
    }

    @Override
    public Contact createContactFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Contact contact = new Contact();
        if(!nullCheckpoint(jsonNode,"firstName")) contact.setFirstName(jsonNode.get("firstName").asText());
        if(!nullCheckpoint(jsonNode,"middleName")) contact.setMiddleName(jsonNode.get("middleName").asText());
        if(!nullCheckpoint(jsonNode,"lastName")) contact.setLastName(jsonNode.get("lastName").asText());
        if(!nullCheckpoint(jsonNode,"email")) contact.setEmail(jsonNode.get("email").asText());
        if(!nullCheckpoint(jsonNode,"mobile")) contact.setMobile(jsonNode.get("mobile").asText());
        if(!nullCheckpoint(jsonNode,"fax")) contact.setFax(jsonNode.get("fax").asText());
        if(!nullCheckpoint(jsonNode,"website")) contact.setWebsite(jsonNode.get("website").asText());
        contact.setStatus(Status.ENABLED);

        //attach org later
        return contact;
    }


    @Override
    public Subscription createSubscription(Order order,Organization organization,User user){
        Subscription sub = new Subscription();
        ProductVariant pv = order.getProductVariant();
        assert pv != null;
        sub.setSeats(1);
        sub.setRenewNextSubscription(true);

        if (pv.getVariantAttributeValue().equals("YEARLY")){
            sub.setValidFrom(LocalDate.now());
            sub.setValidTill(LocalDate.now().plusYears(1));
            sub.setSubscriptionType(SubscriptionTypeEnum.YEARLY);
            sub.setNextRenewalDate(LocalDate.now().plusYears(1).plusDays(1));
        }else {
            sub.setValidFrom(LocalDate.now());
            sub.setValidTill(LocalDate.now().plusMonths(1));
            sub.setSubscriptionType(SubscriptionTypeEnum.MONTHLY);
            sub.setNextRenewalDate(LocalDate.now().plusMonths(1).plusDays(1));
        }
        sub.setSubscriptionStatus(SubscriptionStatusEnum.ACTIVE);
        sub.setOrg(organization);
        List<Application> applications = new ArrayList<>();
        for (BundleItem bi:pv.getBundleItems()) {
            applications.add(bi.getBundleItem().getApplication());
        }
        sub.setApplications(applications);

        sub.setOrder(order);
        sub.setProduct(order.getProduct());
        sub.setProductVariant(pv);
        sub.setExtension(false);
        entityManager.persist(sub);

        if (user != null ){
            SubscriptionUser subscriptionUser = new SubscriptionUser();
            subscriptionUser.setSubscription(sub);
            subscriptionUser.setUser(user);
            entityManager.persist(subscriptionUser);

            // occupy seats from sub
            sub.setOccupiedSeats(calculateUtilService.nullOrZeroValue(sub.getOccupiedSeats(),0) + 1);
            entityManager.persist(sub);
        }

        return sub;
    }

    @Override
    public BillingAccount createBillingAccount(PaymentIntent paymentIntent,Organization organization,Boolean primaryAccount){
        List<BillingAccount> billingAccount = billingAccountService.getBillingAccountByStripeId(paymentIntent.getPaymentMethod());
        if (billingAccount.size() >= 1){
            if (billingAccount.get(0).getOrganization() == null){
                billingAccount.get(0).setOrganization(organization);
                entityManager.persist(billingAccount);
            }
            return billingAccount.get(0);
        }else {
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

    private PaymentMethod fetchPaymentMethod(String id){
        try {
            PaymentMethod pm = PaymentMethod.retrieve(id);
            return pm;
        } catch (StripeException e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    @Override
    public PostpaidAccountResponse setUpAccountForPostpaidVariant(String orderRefNo, String paymentMethodId,BillingAccount billingAccount) throws JsonProcessingException {
        OrderPayload op = orderPayloadService.findByOrderRefNo(orderRefNo);
        if (op == null) throw new ValidationException("order not found");
        Order order = op.getOrder();
        JsonNode jsonNode = objectMapper.readTree(op.getPayload());
        Organization parentOrg = order.getParentOrganization();

        //create org
        Organization org = op.getPayloadType().equals("OrganizationRegistration") ? createOrganizationFromPayload(jsonNode.get("organizationDetails").toString()) :createOrganizationFromPayload(op.getPayload());
        org.setParent(parentOrg);
        org.setStripeCustomerId(op.getStripeCustomerId());
        org.setEmailDomain(op.getPayloadType().equals("OrganizationRegistration") ? jsonNode.get("organizationDetails").get("emailDomain").asText() : jsonNode.get("emailDomain").asText());
        if (parentOrg != null) org.setAccount(parentOrg.getAccount());
        entityManager.persist(org);

        //create address
        Address address = op.getPayloadType().equals("OrganizationRegistration") ? createAddressFromPayload(jsonNode.get("address").toString()) : createAddressFromPayload(op.getPayload());
        address.setOrg(org);
        address.setPrimaryAddress(true);
        entityManager.persist(address);

        //create contact
        Contact contact = op.getPayloadType().equals("OrganizationRegistration") ? createContactFromPayload(jsonNode.get("contact").toString()) : createContactFromPayload(op.getPayload());
        contact.setOrg(org);
        contact.setPrimaryContact(true);
        entityManager.persist(contact);

        PostpaidAccountResponse response = new PostpaidAccountResponse();

        if (op.getPayloadType().equals("OrganizationRegistration")){
            //create account
            Account account = createAccountFromPayload(op.getPayload());
            account.setPrimaryContact(contact);
            account.setPrimaryOrganization(org);
            entityManager.persist(account);

            //set account in org
            org.setEmailDomain(jsonNode.get("organizationDetails").get("emailDomain").asText());
            org.setAccount(account);
            entityManager.persist(org);

            //create user
            User user = new User();
            user.setUsername(jsonNode.get("userName").asText().toLowerCase());
            user.setOrganization(org);
            user.setLastPasswordChangeDate(LocalDateTime.now());
            user.setLastEmailChangeDate(LocalDateTime.now());
            user.setStayInformedAboutBizio(jsonNode.get("stayInformedAboutBizio").asBoolean());
            user.setTermsConditionsAgreed(jsonNode.get("termsConditionsAgreed").asBoolean());
            if(user.getTermsConditionsAgreed()) user.setTermsConditionsAgreedTimestamp(LocalDateTime.now());
            user.setStatus(Status.ENABLED);
            entityManager.persist(user);

            response.setUser(user);

            //set account in address
            address.setAccount(account);
            entityManager.persist(address);

            //set account in contact
            contact.setAccount(account);
            entityManager.persist(contact);

            PaymentIntent pi = new PaymentIntent();
            pi.setPaymentMethod(paymentMethodId);

            createBillingAccount(pi,org,true);
        }

        Subscription sub = createSubscription(order,org,null);
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
    public AddressResolutionModel validateBillingAddress(BillingAddress address) throws Exception {
        return avalaraService.addressValidate(address);
    }

    @Override
    public void validateBillingAccountExpiry(BillingAccount billingAccount) {
        if (billingAccount.getBillingAccType().equals(BillingAccType.CARD)){
            if (Long.parseLong(billingAccount.getExpYear()) < LocalDate.now().getYear()) throw new ValidationException("card has been expired");
            if (Long.parseLong(billingAccount.getExpYear()) == LocalDate.now().getYear()) {
                if (Long.parseLong(billingAccount.getExpMonth()) < LocalDate.now().getMonthValue()) throw new ValidationException("card has been expired");
            }
        }
    }

    @Override
    public PaymentIntent payment(String stripePaymentMethodId, String stripeCustomerId,Long amount,String orderRefNo){
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
