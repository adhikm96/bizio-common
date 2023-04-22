package com.thebizio.commonmodule.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.CouponType;
import com.thebizio.commonmodule.enums.OrderStatus;
import com.thebizio.commonmodule.exception.ValidationException;
import net.avalara.avatax.rest.client.models.TransactionModel;
import net.avalara.avatax.rest.client.models.TransactionSummary;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderFlowImpl implements IOrderFlow {

    final PromotionService promotionService;
    final CalculateUtilService calculateUtilService;

    public OrderFlowImpl(PromotionService promotionService, CalculateUtilService calculateUtilService) {
        this.promotionService = promotionService;
        this.calculateUtilService = calculateUtilService;
    }

    @Transactional
    @Override
    public Order createOrder(@NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion) {
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

        BigDecimal tax = BigDecimal.ZERO;
        List<TransactionSummary> transactionSummaryList = new ArrayList<>();

        if(!isFullDiscount){
            TransactionModel tm = null;

            // pending
            // get taxes for given address
//            try {
//                tm = avalaraService.createTransaction(reqDto.getBillingAddress(), productVariant, user.getUserName(),
//                        DocumentType.SalesOrder, totalWithDiscount);
//            } catch (Exception e) {
//
//                if (e instanceof AvaTaxClientException) {
//                    throw new ValidationException(((AvaTaxClientException) e).getErrorResult().getError().getMessage());
//                } else {
//                    throw new RuntimeException(e);
//                }
//            }

//            tax = calculateUtilService.nullOrZeroValue(tm.getTotalTax(), BigDecimal.ZERO);
//            transactionSummaryList = tm.getSummary();
        }

        // call avalara get taxes


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

        return order;
    }

    @Override
    public String checkout(Order order) {
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(order.getNetTotal().longValue() * 100)
                        .setCurrency("usd")
                        .putMetadata("order_ref", order.getRefNo())
                        .build();

        try {
            PaymentIntent paymentIntent = PaymentIntent.create(params);
            return paymentIntent.getClientSecret();
        } catch (StripeException e) {
            return null;
        }
    }
}
