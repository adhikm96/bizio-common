package com.thebizio.commonmodule.dto.lead;

import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.enums.AccType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor@NoArgsConstructor
public class LeadRegistrationDto {

    //account details
    @NotNull
    private AccType accountType;

    //if account type is individual
    @Valid
    private PersonalDetails personalDetails;

    //if account type is organization
    private OrganizationDetails organizationDetails;

    //for both account type
    @Valid
    private LeadContactDto contact;

    @Valid
    private BillingAddress address;

    @NotNull
    private boolean stayInformedAboutBizio;
    @NotNull
    private boolean termsConditionsAgreed;
}
