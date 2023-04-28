package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.Status;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
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
