package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.AccType;
import com.thebizio.commonmodule.enums.GenderEnum;
import com.thebizio.commonmodule.enums.LeadStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "leads")
@Getter
@Setter
public class Lead extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private LeadStatus status;

    // for both acc
    private AccType accType;
    private String firstName;
    private String lastName;

    // individual acc
    private LocalDate dob;
    private GenderEnum gender;

    //org primary user
    private String jobTitle;
    private String workEmail;
    private String phoneNumber;

    // for both acc
    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipcode;

    //for both acc
    private String signupEmail;
    private String mobile;

    // for org acc
    private String orgName;
    private String website;
    private String taxId;
    private String typeOfBusiness;
    private String subdomain;

    // for both acc
    private boolean stayInformedAboutBizio;
    private boolean termsConditionsAgreed;

}
