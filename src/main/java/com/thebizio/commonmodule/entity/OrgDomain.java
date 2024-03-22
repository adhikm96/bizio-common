package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.DomainStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "org_domains")
public class OrgDomain extends LastUpdateDetail{
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    private DomainStatus status;
    private String domain;
}
