package com.thebizio.commonmodule.service;

import com.stripe.model.PaymentIntent;
import com.stripe.model.SetupIntent;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.entity.*;

import javax.validation.constraints.NotNull;

public interface IOrderFlow {

    String createCustomer(@NotNull String name, @NotNull String email);
    PaymentIntent payment(@NotNull String paymentMethodId, @NotNull String stripeCustId, @NotNull Long amt, @NotNull String orderRefNo);

    OrderPayload createOrderPayload(@NotNull Order order, @NotNull String payloadType, @NotNull String payload, @NotNull String stripeCustId);
    Order createOrder(@NotNull String orgCode, @NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion, @NotNull BillingAddress billingAddress);
    SetupIntent checkout(String stripeCustId);
}
