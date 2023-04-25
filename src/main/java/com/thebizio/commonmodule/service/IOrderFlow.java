package com.thebizio.commonmodule.service;

import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.entity.*;

import javax.validation.constraints.NotNull;

public interface IOrderFlow {

    /**
     *
     * @param name
     * @param email
     * @return stripeCustomerId
     */
    String createCustomer(@NotNull String name, @NotNull String email);

    /**
     *
     * @param paymentMethodId
     * @param stripeCustId
     * @param amt
     * @param orderRefNo
     * @return paymentIntent
     */
    PaymentIntent payment(@NotNull String paymentMethodId, @NotNull String stripeCustId, @NotNull Long amt, @NotNull String orderRefNo);

    /**
     *
     * @param order
     * @param payloadType
     * @param payload
     * @param stripeCustId
     * @return orderPayload
     */
    OrderPayload createOrderPayload(@NotNull Order order, @NotNull String payloadType, @NotNull String payload, @NotNull String stripeCustId);

    /**
     *
     * @param orgCode
     * @param productVariant
     * @param price
     * @param promotion
     * @param billingAddress
     * @return Order
     */
    Order createOrder(@NotNull String orgCode, @NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion, @NotNull BillingAddress billingAddress);

    /**
     *
     * @param stripeCustId
     * @return setupIntent
     */
    SetupIntent checkout(String stripeCustId);
}
