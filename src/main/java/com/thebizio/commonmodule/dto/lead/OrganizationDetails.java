package com.thebizio.commonmodule.dto.lead;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrganizationDetails {

    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String name;
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String website;
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String taxId;
    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String typeOfBusiness;
}
