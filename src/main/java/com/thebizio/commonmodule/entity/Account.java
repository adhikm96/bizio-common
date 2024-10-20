package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.AccType;
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

@Entity @NoArgsConstructor @Table(name = "accounts")
@Getter @Setter
public class Account extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @GeneratorType(type = SecureRandomReferenceIdGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "code", unique = true, nullable = false, updatable = false, length = 64)
    private String code;

    private AccType type;

    @ManyToOne
    @JoinColumn(name = "primary_organization_id")
    private Organization primaryOrganization;

    //remove after migration
    @ManyToOne
    @JoinColumn(name = "primary_contact_id")
    private Contact primaryContact;

    private String signupEmail;
    private String email;
    private String phone;

    private Status status;

    @OneToMany(mappedBy = "account", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    List<AccDomain> accDomains = new ArrayList<>();
}
