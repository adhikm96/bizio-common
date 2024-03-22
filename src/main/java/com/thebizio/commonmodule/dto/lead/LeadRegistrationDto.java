package com.thebizio.commonmodule.dto.lead;

import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.enums.AccType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor@NoArgsConstructor
public class LeadRegistrationDto {

    //account details
    @NotNull
    private AccType accountType;

    //if account type is individual
    private PersonalDetails personalDetails;

    //if account type is organization
    private OrganizationDetails organizationDetails;

    //for both account type
    private LeadContactDto contact;

    private BillingAddress address;

    private boolean stayInformedAboutBizio;
    private boolean termsConditionsAgreed;
}
