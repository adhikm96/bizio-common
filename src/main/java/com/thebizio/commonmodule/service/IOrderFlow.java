package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.model.PaymentIntent;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.dto.OrderResponseDto;
import com.thebizio.commonmodule.entity.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface IOrderFlow {
    String createCustomer(@NotNull String name, @NotNull String email);

    PaymentIntent payment(@NotNull String stripePaymentMethodId, @NotNull String stripeCustomerId, @NotNull Long amount, @NotNull String orderRefNo);

    void createOrderPayload(@NotNull Order order, @NotNull String payloadType, @NotNull String payload, @NotNull String stripeCustomerId);

    Order createOrder(@NotNull String orgCode, @NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion, @Valid @NotNull BillingAddress billingAddress);

    String checkout(@NotNull String stripeCustId);

    Organization createOrganizationFromPayload(@NotNull String payload) throws JsonProcessingException;

    Account createAccountFromPayload(@NotNull String payload) throws JsonProcessingException;

    Subscription createSubscription(@NotNull Order order,@NotNull Organization organization);

    Address createAddressFromPayload(@NotNull String payload) throws JsonProcessingException;

    Contact createContactFromPayload(@NotNull String payload) throws JsonProcessingException;

    OrderResponseDto createOrderResponse(@NotNull Order order,@NotNull String stripeCustId);
}
