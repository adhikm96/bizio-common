package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.AccDomain;
import com.thebizio.commonmodule.entity.Account;
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

    public String getRandomTxt() {
        return "bizio-verification=" + UUID.randomUUID() + "-" + UUID.randomUUID();
    }

    @Override
    public AccDomain createAccDomain(Account acc, String domain, DomainStatus status) {
        AccDomain accDomain = new AccDomain();
        accDomain.setAccount(acc);
        accDomain.setDomain(domain);
        accDomain.setStatus(status);
        accDomain.setDomainDnsRecord(getRandomTxt());
        entityManager.persist(accDomain);
        return accDomain;
    }
}
