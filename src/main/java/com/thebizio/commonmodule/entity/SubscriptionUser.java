package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter @Setter
@Table(name = "subscription_users")
public class SubscriptionUser {
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    @JsonBackReference
    private Subscription subscription;

    private Status status;
}
