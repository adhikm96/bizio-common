package com.thebizio.commonmodule.dto;

import com.stripe.param.PaymentLinkCreateParams;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data @NoArgsConstructor @AllArgsConstructor
public class CheckoutReqDto {
    @NotNull @NotBlank
    private String stripeCustomerId;
    private Set<PaymentLinkCreateParams.PaymentMethodType> paymentMethods;
    private boolean primaryAccount;
    private boolean doCreate;
    private String orderRefNo;
}
