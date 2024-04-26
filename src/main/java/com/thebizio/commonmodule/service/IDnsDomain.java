package com.thebizio.commonmodule.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.stripe.model.PaymentIntent;
import com.thebizio.commonmodule.dto.*;
import com.thebizio.commonmodule.dto.lead.LeadRegistrationDto;
import com.thebizio.commonmodule.dto.tax.TaxAddress;
import com.thebizio.commonmodule.entity.*;
import com.thebizio.commonmodule.enums.DomainStatus;
import com.thebizio.commonmodule.enums.InvoiceStatus;
import com.thebizio.commonmodule.enums.PaymentStatus;
import com.thebizio.commonmodule.exception.InvalidAddressException;
import com.thebizio.commonmodule.exception.TaxSubmissionException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public interface IDnsDomain {

    AccDomain createAccDomain(Account acc, String domain, DomainStatus status);
}
