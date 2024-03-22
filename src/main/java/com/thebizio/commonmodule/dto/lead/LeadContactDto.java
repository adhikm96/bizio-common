package com.thebizio.commonmodule.dto.lead;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data@AllArgsConstructor@NoArgsConstructor
public class LeadContactDto {

    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String mobile;

    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String signupEmail;
}
