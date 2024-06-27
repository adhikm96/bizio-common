package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.entity.AccDomain;
import com.thebizio.commonmodule.entity.Account;
import com.thebizio.commonmodule.entity.Organization;
import com.thebizio.commonmodule.enums.DomainStatus;

public interface IDnsDomain {

    AccDomain createAccDomain(Account acc, Organization org, String domain, DomainStatus status);
}
