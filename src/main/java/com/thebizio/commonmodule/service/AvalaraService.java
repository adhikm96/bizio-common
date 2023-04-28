package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.entity.ProductVariant;
import com.thebizio.commonmodule.exception.ValidationException;
import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.TransactionBuilder;
import net.avalara.avatax.rest.client.enums.DocumentType;
import net.avalara.avatax.rest.client.enums.TextCase;
import net.avalara.avatax.rest.client.enums.TransactionAddressType;
import net.avalara.avatax.rest.client.models.AddressResolutionModel;
import net.avalara.avatax.rest.client.models.TransactionModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("commonAvalaraService")
public class AvalaraService {
    private static final String COUNTRY = "US";

    private final String avalaraCompanyCode;

    private final AvaTaxClient avaTaxClient;

    private final String avalaraTaxCode;

    private final BigDecimal quantity = new BigDecimal(1);

    public AvalaraService(@Value("${avalara-company-code}") String avalaraCompanyCode, AvaTaxClient avaTaxClient,@Value("${avalara-tax-code") String avalaraTaxCode) {
        this.avalaraCompanyCode = avalaraCompanyCode;
        this.avaTaxClient = avaTaxClient;
        this.avalaraTaxCode = avalaraTaxCode;
    }

    public TransactionModel createTransaction(
            BillingAddress ba, ProductVariant productVariant, String orgCode, DocumentType dt,
            BigDecimal productWithDiscount
    ) throws Exception {
        AddressResolutionModel arm = addressValidate(ba);
        return new TransactionBuilder(
                    avaTaxClient,
                    avalaraCompanyCode,
                    dt,
                    orgCode
                )
                .withAddress(
                        TransactionAddressType.SingleLocation,
                        arm.getValidatedAddresses().get(0).getLine1(),
                        arm.getValidatedAddresses().get(0).getLine2(),
                        null,
                        arm.getValidatedAddresses().get(0).getCity(),
                        arm.getValidatedAddresses().get(0).getRegion(),
                        arm.getValidatedAddresses().get(0).getPostalCode(),
                        COUNTRY
                )
                .withLine(
                        productWithDiscount ,
                        quantity,
                        avalaraTaxCode,
                        productVariant.getProduct().getCode(),
                        productVariant.getProduct().getName()
                )
                .Create();
    }

    public AddressResolutionModel addressValidate(BillingAddress ba) throws Exception {

        AddressResolutionModel arm = avaTaxClient.resolveAddress(
                ba.getAddressLine1(),
                null,
                null,
                ba.getCity(),
                ba.getState(),
                ba.getZipcode(),
                COUNTRY,
                TextCase.Mixed
        );

        String errorMsg = "invalid address";

        if (arm.getValidatedAddresses().size() == 0) throw new ValidationException(errorMsg);

        String addressType =  arm.getValidatedAddresses().get(0).getAddressType();

        if(addressType == null) throw new ValidationException(errorMsg);

        if(!addressType.equals("UnknownAddressType")) return arm;


        if(arm.getMessages() == null || arm.getMessages().size() == 0) throw new ValidationException(errorMsg);

        if (!arm.getMessages().get(0).getSummary().isEmpty())
            errorMsg = arm.getMessages().get(0).getSummary();
        else if (!arm.getMessages().get(0).getDetails().isEmpty())
            errorMsg = arm.getMessages().get(0).getDetails();

        throw new ValidationException(errorMsg);
    }
}


