package com.thebizio.commonmodule.service.tax;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.taxjar.Taxjar;
import com.taxjar.exception.TaxjarException;
import com.taxjar.model.taxes.Tax;
import com.taxjar.model.validations.Address;
import com.taxjar.model.validations.AddressResponse;
import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.dto.tax.TaxAddress;
import com.thebizio.commonmodule.dto.tax.TaxResp;
import com.thebizio.commonmodule.exception.InvalidAddressException;
import com.thebizio.commonmodule.exception.TaxCalculationException;
import com.thebizio.commonmodule.exception.TaxSubmissionException;
import com.thebizio.commonmodule.service.CalculateUtilService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
public class TaxJarService implements ITaxService {
    final Taxjar taxjar;
    final ObjectMapper objectMapper;
    final ModelMapper modelMapper;
    final String productTaxCode;

    public TaxJarService(Taxjar taxjar, ObjectMapper objectMapper, ModelMapper modelMapper, @Value("${product-taxcode}") String productTaxCode) {
        this.taxjar = taxjar;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.productTaxCode = productTaxCode;
    }

    public TaxResp calculateTax(BillingAddress ba, BigDecimal grossTotal, BigDecimal discount) throws TaxCalculationException, JsonProcessingException {
        TaxAddress address;
        try {
            address = getAddress(ba);
        } catch (InvalidAddressException exception) {
            throw new TaxCalculationException("incorrect address");
        }

        Map<String, Object> params = new HashMap<>();

        setFromDetails(params);
        setToDetails(params, address);

        params.put("amount", grossTotal);
        params.put("discount", discount);
        params.put("shipping", 0.0);

        List<Map> lineItems = new ArrayList();
        Map<String, Object> lineItem = new HashMap<>();
        lineItem.put("quantity", 1);
        lineItem.put("product_tax_code", productTaxCode);
        lineItem.put("unit_price", grossTotal);
        lineItem.put("discount", discount);
        lineItems.add(lineItem);

        params.put("line_items", lineItems);

        try {
            Tax tax = taxjar.taxForOrder(params).tax;
            return new TaxResp(tax.getAmountToCollect() != null ? CalculateUtilService.roundTwoDigits(tax.getAmountToCollect().doubleValue()) : 0.0, objectMapper.writeValueAsString(tax.getBreakdown()));
        } catch (TaxjarException e) {
            throw new TaxCalculationException(e);
        }
    }

    private void setToDetails(Map<String, Object> params, TaxAddress address) {
        params.put("to_country", address.getCountry());
        params.put("to_zip", address.getZip());
        params.put("to_state", address.getState());
        params.put("to_city", address.getCity());
        params.put("to_street", address.getStreet());
    }

    private void setFromDetails(Map<String, Object> params) {
        params.put("from_country", "US");
        params.put("from_zip", "33185");
        params.put("from_state", "FL");
        params.put("from_city", "Miami");
        params.put("from_street", "16207 SW 53rd Ter");
    }

    @Override
    public void submitTax(BillingAddress ba, BigDecimal grossTotal, BigDecimal discount, BigDecimal tax, String transactionId, String productIdentifier, String lineItemId, String custId) throws TaxSubmissionException {
        TaxAddress address;
        try {
            address = getAddress(ba);
        } catch (InvalidAddressException exception) {
            throw new TaxSubmissionException("incorrect address");
        }

        Map<String, Object> params = new HashMap<>();

        params.put("transaction_id", transactionId);
        params.put("transaction_date", LocalDateTime.now().toString());
        params.put("sales_tax", tax);

        setFromDetails(params);
        setToDetails(params, address);

        params.put("amount", grossTotal.subtract(discount));
        params.put("shipping", 0.0);
        params.put("customer_id", custId);

        List<Map> lineItems = new ArrayList<>();
        Map<String, Object> lineItem = new HashMap<>();

        lineItem.put("id", lineItemId);
        lineItem.put("quantity", 1);
        lineItem.put("sales_tax", tax);
        lineItem.put("discount", discount);
        lineItem.put("unit_price", grossTotal);
        lineItem.put("product_identifier", productIdentifier);

        lineItems.add(lineItem);

        params.put("line_items", lineItems);

        try {
            taxjar.createOrder(params).order.getUserId();
        } catch (TaxjarException e) {
            throw new TaxSubmissionException(e);
        }
    }

    public TaxAddress getAddress(BillingAddress ba) throws InvalidAddressException {
        Map<String, Object> addParams = new HashMap<>();

        addParams.put("country", ba.getCountry());
        addParams.put("zip", ba.getZipcode());
        addParams.put("state", ba.getState());
        addParams.put("city", ba.getCity());
        addParams.put("street", ba.getAddressLine1());

        AddressResponse addressResponse;
        try {
            addressResponse = taxjar.validateAddress(addParams);
        } catch (TaxjarException e) {
            throw new InvalidAddressException(e);
        }

        if(addressResponse.addresses.isEmpty()) throw new InvalidAddressException("invalid address");

        Address address = addressResponse.addresses.get(0);
        return modelMapper.map(address, TaxAddress.class);
    }
}