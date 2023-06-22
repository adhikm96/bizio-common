package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.SubscriptionStatusEnum;
import com.thebizio.commonmodule.enums.SubscriptionTypeEnum;
import com.thebizio.commonmodule.generator.IRandomGeneratorField;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
public class Subscription extends LastUpdateDetail implements IRandomGeneratorField {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @GeneratorType(type = SecureRandomReferenceIdGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "name", unique = true, nullable = false, updatable = false, length = 64)
    private String name;

    private Integer seats;

    @Column(columnDefinition = "int default 0")
    private Integer occupiedSeats;

    private LocalDate validFrom;
    private LocalDate validTill;
    private LocalDate nextRenewalDate;
    private SubscriptionTypeEnum subscriptionType;
    private SubscriptionStatusEnum subscriptionStatus;

    @Column(columnDefinition = "boolean default false")
    private Boolean bizioGuestSubscription;

    @Column(columnDefinition = "boolean default false")
    private Boolean renewNextSubscription;

    private boolean extension;

    @ManyToOne
    @JoinColumn(name = "extension_of")
    private Subscription extensionOf;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization org;

    @ManyToMany
    @JoinTable(name = "subscription_apps", joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id"))
    private List<Application> applications = new ArrayList<>();

//    @ManyToMany
//    @JoinTable(name = "subscription_users", joinColumns = @JoinColumn(name = "subscription_id"),
//            inverseJoinColumns = @JoinColumn(name = "user_id"))

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY)
    private List<SubscriptionUser> subUsers = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    @JsonBackReference
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "preferred_billing_account_id")
    @JsonBackReference
    private BillingAccount preferredBillingAccount;

    @Override
    public String getRandomGeneratorField() {
        return "name";
    }


}
