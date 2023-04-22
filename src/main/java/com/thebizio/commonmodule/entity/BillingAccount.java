package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.BillingAccType;
import com.thebizio.commonmodule.enums.CardType;
import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "billing_accounts")
@Data
@NoArgsConstructor
public class BillingAccount extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private BillingAccType billingAccType;
    private String cardBrand;
    private CardType cardType;
    private String expMonth;
    private String expYear;
    private String accHolderType;
    private String accHolderName;
    private String bankName;
    private String bankRoutingNumber;
    private String financialConnectionsAcc;
    private String fingerprint;
    private String last4;
    private Status status;
    private String pgAccType;
    private String stripePaymentMethodId;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JsonBackReference
    private Organization organization;

}
