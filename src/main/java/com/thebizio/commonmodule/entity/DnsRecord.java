package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.DnsRecordType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "dns_records")
public class DnsRecord extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "acc_domain_id")
    private AccDomain accDomain;

    private DnsRecordType type;
    private String name;
    private Integer ttl;
    private String value;
}
