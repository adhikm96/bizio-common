package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.SubscriptionStatusEnum;
import com.thebizio.commonmodule.enums.SubscriptionTypeEnum;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "subscriptions")
@Data
@NoArgsConstructor
public class Subscription extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private Integer seats;
    private LocalDate validFrom;
    private LocalDate validTill;
    private LocalDate nextRenewalDate;
    private SubscriptionTypeEnum subscriptionType;
    private SubscriptionStatusEnum subscriptionStatus;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization org;

    @ManyToMany
    @JoinTable(name = "subscription_apps", joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "application_id"))
    private List<Application> applications = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "subscription_users", joinColumns = @JoinColumn(name = "subscription_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

}
