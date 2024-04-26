package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.AccDomain;
import com.thebizio.commonmodule.entity.Account;
import com.thebizio.commonmodule.entity.DnsRecord;
import com.thebizio.commonmodule.enums.DnsRecordType;
import com.thebizio.commonmodule.enums.DomainStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.UUID;

@Service
public class DnsDomainImpl implements IDnsDomain {

    Logger logger = LoggerFactory.getLogger(DnsDomainImpl.class);
    private final EntityManager entityManager;


    public DnsDomainImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public DnsRecord createTxtDnsRecord(AccDomain accDomain, String name, Integer ttl) {
        DnsRecord dnsRecord = new DnsRecord();
        dnsRecord.setAccDomain(accDomain);
        dnsRecord.setName(name);
        dnsRecord.setTtl(ttl);
        dnsRecord.setType(DnsRecordType.TXT);
        dnsRecord.setValue(getRandomTxt());
        entityManager.persist(dnsRecord);
        return dnsRecord;
    }

    public String getRandomTxt() {
        return "bizio-verification=" + UUID.randomUUID() + "-" + UUID.randomUUID();
    }

    @Override
    public DnsRecord createMxDnsRecord(AccDomain accDomain, String name, Integer ttl, String value) {
        DnsRecord dnsRecord = new DnsRecord();
        dnsRecord.setAccDomain(accDomain);
        dnsRecord.setName(name);
        dnsRecord.setTtl(ttl);
        dnsRecord.setType(DnsRecordType.MX);
        dnsRecord.setValue(value);
        entityManager.persist(dnsRecord);
        return dnsRecord;
    }

    @Override
    public AccDomain createAccDomain(Account acc, String domain, DomainStatus status) {
        AccDomain accDomain = new AccDomain();
        accDomain.setAccount(acc);
        accDomain.setDomain(domain);
        accDomain.setStatus(status);
        entityManager.persist(accDomain);
        return accDomain;
    }
}
