package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.model.PaymentIntent;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.dto.CheckoutReqDto;
import com.thebizio.commonmodule.dto.OrderResponseDto;
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.InvoiceStatus;
import com.thebizio.commonmodule.enums.PaymentStatus;
import net.avalara.avatax.rest.client.models.AddressResolutionModel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public interface IOrderFlow {
    String createCustomer(@NotNull String name, @NotNull String email);

    PaymentIntent payment(@NotNull String stripePaymentMethodId, @NotNull String stripeCustomerId, @NotNull Long amount, @NotNull String orderRefNo);

    void createOrderPayload(@NotNull Order order, @NotNull String payloadType, @NotNull String payload, @NotNull String stripeCustomerId);

    Order createOrder(@NotNull String orgCode, @NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion, @Valid @NotNull BillingAddress billingAddress) throws JsonProcessingException;

    String checkout(@NotNull String stripeCustId);

    String checkout(@NotNull CheckoutReqDto dto);

    Organization createOrganizationFromPayload(@NotNull String payload) throws JsonProcessingException;

    Account createAccountFromPayload(@NotNull String payload) throws JsonProcessingException;

    Subscription createSubscription(@NotNull Order order,@NotNull Organization organization,User user);

    Address createAddressFromPayload(@NotNull String payload) throws JsonProcessingException;

    Contact createContactFromPayload(@NotNull String payload) throws JsonProcessingException;

    OrderResponseDto createOrderResponse(@NotNull Order order,@NotNull String stripeCustId,String clientSecretKey) throws JsonProcessingException;

    void submitTaxToAvalara(@NotNull Order order,@NotNull String orgCode) throws Exception;

    void createInvoiceFromOrder(@NotNull Order order,@NotNull Subscription sub,@NotNull InvoiceStatus status,@NotNull Payment payment);

    Payment createPayment(@NotNull BigDecimal amount,@NotNull BillingAccount ba,@NotNull PaymentStatus status);

    BillingAccount createBillingAccount(@NotNull PaymentIntent paymentIntent,@NotNull Organization organization,@NotNull Boolean primaryAccount);

    AddressResolutionModel validateBillingAddress(@NotNull BillingAddress address) throws Exception;

    void validateBillingAccountExpiry(@NotNull BillingAccount billingAccount);
}
