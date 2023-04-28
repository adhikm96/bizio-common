package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SetupIntentCreateParams;

import com.thebizio.commonmodule.dto.AddOnsDto;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.dto.OrderResponseDto;
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.*;
import com.thebizio.commonmodule.exception.ServerException;
import com.thebizio.commonmodule.exception.ValidationException;
import net.avalara.avatax.rest.client.enums.DocumentType;
import net.avalara.avatax.rest.client.models.TransactionModel;
import net.avalara.avatax.rest.client.models.TransactionSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderFlowImpl implements IOrderFlow {

    Logger logger = LoggerFactory.getLogger(OrderFlowImpl.class);

    private final PromotionService promotionService;

    private final CalculateUtilService calculateUtilService;

    private final AvalaraService avalaraService;

    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;

    public OrderFlowImpl(PromotionService promotionService, CalculateUtilService calculateUtilService, AvalaraService avalaraService, EntityManager entityManager, ObjectMapper objectMapper) {
        this.promotionService = promotionService;
        this.calculateUtilService = calculateUtilService;
        this.avalaraService = avalaraService;
        this.entityManager = entityManager;
        this.objectMapper = objectMapper;
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
    ) {
        Order order = new Order();

        if (!price.getProductVariant().getId().equals(productVariant.getId()))
            throw new ValidationException("priceId don't belong to variantId");

        // check subscription already exists
        // expire old orders

        // check promotion is valid

        boolean isPromotion = promotion != null;

        if (isPromotion && !promotion.isValid()) throw new ValidationException("promotion is not valid");
        boolean isFullDiscount = false;

        Double discount = null;

        Double grossTotal = price.getPrice();

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
            order.setDiscountStr("{"+promotion.getCode()+":"+discount+"}");
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

            // need to decide what to do with transactionSummaryList
            transactionSummaryList = tm.getSummary();
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

    public OrderResponseDto createOrderResponse(Order order,String stripeCustId){
        OrderResponseDto dto = new OrderResponseDto();
        dto.setProductName(order.getProductVariant().getProduct().getName());
        dto.setProductCode(order.getProductVariant().getProduct().getCode());
        dto.setAttributeValue(order.getProductVariant().getAttributeValue());
        dto.setSubTotal(order.getGrossTotal());

        //need to add this field in order
        //dto.setTax(order.getTax());
        dto.setTaxStr(order.getTaxes());
        dto.setDiscount(order.getDiscount());
        dto.setDiscountStr(order.getDiscountStr());
        dto.setNetTotal(order.getNetTotal());

        List<AddOnsDto> addons = new ArrayList<>();
        for (ProductVariant addOn: order.getProductVariant().getAddOns()) {
            AddOnsDto addOnDto = new AddOnsDto();
            addOnDto.setName(addOn.getName());
            addOnDto.setPrice(BigDecimal.valueOf(addOn.getDefaultPrice()));
            addons.add(addOnDto);
        }
        dto.setAddons(addons);
        dto.setOrderRefNo(order.getRefNo());
        dto.setStripeCustomerId(stripeCustId);
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
        org.setName(jsonNode.get("came").asText());
        org.setDescription(jsonNode.get("description").asText());
        org.setIndustry(jsonNode.get("industry").asText());
        org.setClassName(jsonNode.get("className").asText());
        org.setStructure(jsonNode.get("structure").asText());
        org.setIndustryType(jsonNode.get("industryType").asText());
        org.setExchange(jsonNode.get("exchange").asText());
        org.setMarket(jsonNode.get("market").asText());
        org.setSymbol(jsonNode.get("symbol").asText());
        org.setStatus(Status.ENABLED);

        //attach parent org and account later

        return org;
    }

    @Override
    public Address createAddressFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Address address = new Address();
        if(jsonNode.has("addressLine1")) address.setAddressLine1(jsonNode.get("addressLine1").asText());
        if(jsonNode.has("addressLine2")) address.setAddressLine2(jsonNode.get("addressLine2").asText());
        address.setStatus(Status.ENABLED);
        address.setCity(jsonNode.get("city").asText());
        address.setState(jsonNode.get("state").asText());
        address.setCountry(jsonNode.get("country").asText());
        address.setZipcode(jsonNode.get("zipcode").asText());

        //attach org later
        return address;
    }

    @Override
    public Contact createContactFromPayload(String payload) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(payload);
        Contact contact = new Contact();
        if(jsonNode.has("firstName")) contact.setFirstName(jsonNode.get("firstName").asText());
        if(jsonNode.has("middleName")) contact.setMiddleName(jsonNode.get("middleName").asText());
        if(jsonNode.has("lastName")) contact.setLastName(jsonNode.get("lastName").asText());
        if(jsonNode.has("email")) contact.setEmail(jsonNode.get("email").asText());
        if(jsonNode.has("phone")) contact.setPhone(jsonNode.get("phone").asText());
        if(jsonNode.has("mobile")) contact.setMobile(jsonNode.get("mobile").asText());
        if(jsonNode.has("fax")) contact.setFax(jsonNode.get("fax").asText());
        if(jsonNode.has("website")) contact.setWebsite(jsonNode.get("website").asText());
        if(jsonNode.has("email")) contact.setEmail(jsonNode.get("email").asText());
        contact.setStatus(Status.ENABLED);

        //attach org later
        return contact;
    }


    @Override
    public Subscription createSubscription(Order order,Organization organization){
        Subscription sub = new Subscription();
        sub.setName(order.getProductVariant().getProduct().getName());

        //seats should be added to product/product variant
        sub.setSeats(10);

        if (order.getProductVariant().getAttributeValue().equals("YEARLY")){
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
        return sub;
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
