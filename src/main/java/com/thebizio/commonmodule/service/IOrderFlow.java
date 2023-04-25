package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.thebizio.commonmodule.entity.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public interface IOrderFlow {

    String createCustomer(@NotNull String name, @NotNull String email);

    PaymentIntent payment(@NotNull String stripePaymentMethodId, @NotNull String stripeCustomerId, @NotNull Long amount, @NotNull String orderRefNo);

    void createOrderPayload(@NotNull Order order,@NotNull String payloadType,@NotNull String payload,@NotNull String stripeCustomerId);

    Order createOrder(@NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion);

    //    String checkout(Order order);
    String createPaymentMethodIntent(String stripeCustomerId);

    Organization createOrganizationFromPayload(String payload) throws JsonProcessingException;

    Account createAccountFromPayload(String payload) throws JsonProcessingException;

    Subscription createSubscription(Order order,Organization organization);

    Address createAddressFromPayload(String payload) throws JsonProcessingException;

    Contact createContactFromPayload(String payload) throws JsonProcessingException;
}
