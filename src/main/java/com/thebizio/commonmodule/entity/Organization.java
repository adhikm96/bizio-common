package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @Table(name = "organizations")
public class Organization extends LastUpdateDetail {
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @Column(updatable = false)
    private String code;

    private String description;

    @ManyToOne
    @JoinColumn(name = "parent_id", updatable = false, nullable = true)
    private Organization parent;

    private String industry;
    private String className;
    private String structure;
    private String industryType;
    private String exchange;
    private String market;
    private String symbol;
    private Status status;
    private String stripeCustomerId;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
