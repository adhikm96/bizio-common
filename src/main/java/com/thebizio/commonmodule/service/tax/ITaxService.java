package com.thebizio.commonmodule.service.tax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.dto.tax.TaxAddress;
import com.thebizio.commonmodule.dto.tax.TaxResp;
import com.thebizio.commonmodule.exception.InvalidAddressException;
import com.thebizio.commonmodule.exception.TaxCalculationException;
import com.thebizio.commonmodule.exception.TaxSubmissionException;

import java.math.BigDecimal;

public interface ITaxService {
    TaxResp calculateTax(BillingAddress ba, BigDecimal grossTotal, BigDecimal discount) throws TaxCalculationException, JsonProcessingException;
    void submitTax(BillingAddress ba, BigDecimal grossTotal, BigDecimal discount, BigDecimal tax, String transactionId, String productIdentifier, String lineItemId, String orgCode) throws TaxSubmissionException;
    TaxAddress getAddress(BillingAddress ba) throws InvalidAddressException;
}
