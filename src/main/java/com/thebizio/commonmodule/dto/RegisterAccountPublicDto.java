package com.thebizio.commonmodule.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class RegisterAccountPublicDto {

    @NotNull
    @NotBlank
    private String userName;

    @NotNull
    @NotBlank
    @Email
    private String userEmail;

    @NotNull
    @NotBlank
    private String firstName;

    private String middleName;

    @NotNull
    @NotBlank
    private String lastName;

    private String organizationName;

    @NotNull
    @NotBlank
    private String accountType;

    @NotNull
    @NotBlank
    private String country;

    @NotNull
    @NotBlank
    private String state;

    @NotNull(message = "can not be null or empty")
    @NotBlank(message = "can not be null or empty")
    private String city;

    private LocalDate dateOfBirth;
    private boolean stayInformedAboutBizio;
    private boolean termsConditionsAgreed;
    private LocalDateTime termsConditionsAgreedTimestamp;
}
