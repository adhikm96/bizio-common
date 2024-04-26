package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.model.PaymentIntent;
import com.thebizio.commonmodule.dto.*;
import com.thebizio.commonmodule.dto.lead.LeadRegistrationDto;
import com.thebizio.commonmodule.dto.tax.TaxAddress;
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.InvoiceStatus;
import com.thebizio.commonmodule.enums.PaymentStatus;
import com.thebizio.commonmodule.exception.InvalidAddressException;
import com.thebizio.commonmodule.exception.TaxSubmissionException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface IOrderFlow {
    String createCustomer(@NotNull String name, @NotNull String email);

    PaymentIntent payment(@NotNull String stripePaymentMethodId, @NotNull String stripeCustomerId, @NotNull Long amount,String orderRefNo);

    void createContactFromLeadForUser(@NotNull Lead lead, @NotNull User user);

    void createContactFromLeadForOrganization(Lead lead, Organization org);

    void createOrderPayload(@NotNull Order order, @NotNull String payloadType, @NotNull String payload, @NotNull String stripeCustomerId);


    Order createOrder(@NotNull String orgCode, @NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion, @Valid @NotNull BillingAddress billingAddress) throws JsonProcessingException;

    Order createOrder(@NotNull ProductVariant productVariant, @NotNull Price price, Promotion promotion, @NotNull Lead lead) throws JsonProcessingException;
    String checkout(@NotNull String stripeCustId);

    String checkout(@NotNull CheckoutReqDto dto);

    Organization createOrganizationFromPayload(@NotNull String payload) throws JsonProcessingException;

    Account createAccountFromPayload(@NotNull String payload) throws JsonProcessingException;

    Subscription createSubscription(@NotNull Order order,@NotNull Organization organization,User user);

    Address createAddressFromPayload(@NotNull String payload) throws JsonProcessingException;

    Contact createContactFromPayload(@NotNull String payload) throws JsonProcessingException;

    TemporaryOrderResponseDto temporaryOrderResponse(@NotNull ProductVariant productVariant, @NotNull Price price, BillingAddress billingAddress) throws JsonProcessingException;

    OrderResponseDto createOrderResponse(@NotNull Order order,@NotNull String stripeCustId,String clientSecretKey) throws JsonProcessingException;

    void submitTax(@NotNull Order order,@NotNull String custId) throws TaxSubmissionException;

    void submitTax(ProductVariant pv, String custId, Address address, BigDecimal grossTotal, BigDecimal tax, String invoiceRef, BigDecimal discount) throws TaxSubmissionException;

    Invoice createInvoiceFromOrder(@NotNull Order order,@NotNull Subscription sub,@NotNull InvoiceStatus status,@NotNull Payment payment);

    Payment createPayment(@NotNull BigDecimal amount,@NotNull BillingAccount ba,@NotNull PaymentStatus status,@NotNull String paymentRef);

    BillingAccount createBillingAccount(@NotNull PaymentIntent paymentIntent,@NotNull Organization organization,@NotNull Boolean primaryAccount);

    TaxAddress validateBillingAddress(@NotNull BillingAddress address) throws InvalidAddressException;

    void validateBillingAccountExpiry(@NotNull BillingAccount billingAccount);

    PostpaidAccountResponse setUpAccountForPostpaidVariant(String orderRefNo, String paymentMethodId,BillingAccount billingAccount) throws JsonProcessingException;

    Lead createLead(LeadRegistrationDto dto);
}
