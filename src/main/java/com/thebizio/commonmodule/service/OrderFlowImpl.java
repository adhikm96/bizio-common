package com.thebizio.commonmodule.service;

import com.stripe.exception.CardException;
import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.SetupIntentCreateParams;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.CouponType;
import com.thebizio.commonmodule.enums.OrderStatus;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderFlowImpl implements IOrderFlow {

    Logger logger = LoggerFactory.getLogger(OrderFlowImpl.class);

    final PromotionService promotionService;
    final CalculateUtilService calculateUtilService;

    final AvalaraService avalaraService;

    final EntityManager entityManager;

    public OrderFlowImpl(PromotionService promotionService, CalculateUtilService calculateUtilService, AvalaraService avalaraService, EntityManager entityManager) {
        this.promotionService = promotionService;
        this.calculateUtilService = calculateUtilService;
        this.avalaraService = avalaraService;
        this.entityManager = entityManager;
    }

    @Override
    public PaymentIntent payment(String paymentMethodId, String stripeCustId, Long amt, String orderRefNo) {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amt)
                        .setPaymentMethod(paymentMethodId)
                        .setCurrency("usd")
                        .setCustomer(stripeCustId)
                        .setConfirm(true)
                        .setOffSession(true)
                        .putMetadata("order_ref", orderRefNo)
                        .setAutomaticPaymentMethods(
                                PaymentIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build()
                        )
                        .build();

        try {
            return PaymentIntent.create(params);
        } catch (CardException exception) {
            logger.error(exception.getMessage());
            return exception.getStripeError().getPaymentIntent();
        } catch (StripeException exception) {
            logger.error(exception.getMessage());
            throw new ValidationException("try again later or contact support");
        }
    }

    @Override
    public OrderPayload createOrderPayload(Order order, String payloadType, String payload, String stripeCustId) {

        OrderPayload orderPayload = new OrderPayload();
        orderPayload.setOrder(order);
        orderPayload.setPayloadType(payloadType);
        orderPayload.setPayload(payload);
        orderPayload.setStripeCustomerId(stripeCustId);

        entityManager.persist(orderPayload);

        return orderPayload;
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

        if(!price.getProductVariant().getId().equals(productVariant.getId()))
            throw new ValidationException("priceId don't belong to variantId");

        // check subscription already exists
        // expire old orders

        // check promotion is valid

        boolean isPromotion = promotion != null;

        if(isPromotion && !promotion.isValid()) throw new ValidationException("promotion is not valid");
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
        }

        BigDecimal tax = BigDecimal.ZERO;
        List<TransactionSummary> transactionSummaryList = new ArrayList<>();

        if(!isFullDiscount){
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

        if(discount != null){
            order.setNetTotal(BigDecimal.valueOf(grossTotal).add(tax).subtract(BigDecimal.valueOf(discount)));
        }else {
            order.setNetTotal(BigDecimal.valueOf(grossTotal).add(tax));
        }

        if(order.getNetTotal().compareTo(BigDecimal.ZERO) < 0){
            order.setNetTotal(BigDecimal.ZERO);
        }

        if(promotion != null){
            order.setPromoCode(promotion.getCode());
        }

        order.setStatus(OrderStatus.IN_PROGRESS);

        entityManager.persist(order);
        return order;
    }

    @Override
    public SetupIntent checkout(String stripeCustId) {
        SetupIntentCreateParams params = SetupIntentCreateParams
                .builder()
                .setCustomer(stripeCustId)
                .addPaymentMethodType("card")
                .build();
        try {
            return SetupIntent.create(params);
        } catch (StripeException e) {
            logger.error(e.getMessage());
            throw new ValidationException("try again later or contact support");
        }
    }

    public String createCustomer(String name, String email) {
        Map<String, Object> customerMap = new HashMap<>();
        customerMap.put("name", name);
        customerMap.put("email", email);
        try {
            return Customer.create(customerMap).getId();
        } catch (StripeException e) {
            logger.error(e.getMessage());
            throw new ValidationException("try again later or contact support");
        }
    }
}
