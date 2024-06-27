package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.HashMapConvertor;
import com.thebizio.commonmodule.enums.DomainStatus;
import com.thebizio.commonmodule.enums.MxStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "acc_domains")
public class AccDomain extends LastUpdateDetail{
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization organization;

    private DomainStatus status;
    private String domain;
    private String domainDnsRecord;
    private MxStatusEnum mxStatus;

    @Convert(converter = HashMapConvertor.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> dnsMx;

    @Convert(converter = HashMapConvertor.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> dnsSpf;

    @Convert(converter = HashMapConvertor.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> dnsDkim;

    @Convert(converter = HashMapConvertor.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> dnsDmarc;

    @Convert(converter = HashMapConvertor.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> dnsDmarcReport;

    @Convert(converter = HashMapConvertor.class)
    @Column(columnDefinition = "TEXT")
    private Map<String, Object> dnsRecommended;

}
