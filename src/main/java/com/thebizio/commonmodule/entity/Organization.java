package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @Table(name = "organizations")
public class Organization extends LastUpdateDetail {
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    @GeneratorType(type = SecureRandomReferenceIdGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "code", unique = true, nullable = false, updatable = false, length = 64)
    private String code;

    @ManyToOne
    @JoinColumn(name = "parent_id", updatable = false, nullable = true)
    private Organization parent;

    private Status status;
    private String stripeCustomerId;

    private String emailDomain;
    private String subdomain;

    @Column(name = "type_of_business")
    private String typeOfBusiness;

    private String website;

    @Column(name = "tax_id")
    private String taxId;

    @Column(name = "billing_email")
    private String billingEmail;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany
    @JoinTable(name = "organization_users", joinColumns = @JoinColumn(name = "organization_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    List<OrgDomain> orgDomains = new ArrayList<>();
}
