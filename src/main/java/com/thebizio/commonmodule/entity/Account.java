package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.AccType;
import com.thebizio.commonmodule.enums.Status;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "primary_contact_id")
    private Contact primaryContact;

    private Status status;
}
