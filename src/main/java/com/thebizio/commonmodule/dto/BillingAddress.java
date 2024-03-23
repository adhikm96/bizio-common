package com.thebizio.commonmodule.dto;

import com.thebizio.commonmodule.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddress {
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String addressLine1;
    private String addressLine2;
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String city;
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String state;
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String country;
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String zipcode;
}
