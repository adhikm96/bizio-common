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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AvalaraService {

    private String avalaraUsername;

    private String avalaraPassword;

    private String avalaraCompanyCode;

    private String bizioCenterEnv;

    private AvaTaxClient avaTaxClient;

    private String avalaraTaxCode;

    private BigDecimal quantity = new BigDecimal(1);

    public AvalaraService(String avalaraUsername, String avalaraPassword, String avalaraCompanyCode, String bizioCenterEnv, AvaTaxClient avaTaxClient, String avalaraTaxCode) {
        this.avalaraUsername = avalaraUsername;
        this.avalaraPassword = avalaraPassword;
        this.avalaraCompanyCode = avalaraCompanyCode;
        this.bizioCenterEnv = bizioCenterEnv;
        this.avaTaxClient = avaTaxClient;
        this.avalaraTaxCode = avalaraTaxCode;
    }

    public TransactionModel createTransaction(
            BillingAddress ba, ProductVariant productVariant, String orgCode, DocumentType dt,
            BigDecimal productWithDiscount
    ) throws Exception {
        AddressResolutionModel arm =addressValidate(ba);
        return
                new TransactionBuilder(
                    avaTaxClient,
                    avalaraCompanyCode,
                    dt,
                    orgCode
                )
                .withAddress(
                        TransactionAddressType.SingleLocation,
                        arm.getValidatedAddresses().get(0).getLine1(),
                        null,
                        null,
                        arm.getValidatedAddresses().get(0).getCity(),
                        arm.getValidatedAddresses().get(0).getRegion(),
                        arm.getValidatedAddresses().get(0).getPostalCode(),
                        "US"
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
                ba.getStreetAddress(),
                null,
                null,
                ba.getCity(),
                ba.getState(),
                ba.getZip(),
                "US",
                TextCase.Mixed
        );

        if (arm.getValidatedAddresses().size() == 0) {
            throw new ValidationException("invalid address");
        }

        String addressType =  arm.getValidatedAddresses().get(0).getAddressType();

        if(addressType == null){
            throw new ValidationException("invalid address");
        }
        if(!addressType.equals("UnknownAddressType")) {
            return arm;
        }

        if(arm.getMessages() == null || arm.getMessages().size() == 0){
            throw new ValidationException("invalid address");
        }

        if (!arm.getMessages().get(0).getSummary().isEmpty()){
            throw new ValidationException(arm.getMessages().get(0).getSummary());
        } else if (!arm.getMessages().get(0).getDetails().isEmpty()) {
            throw new ValidationException(arm.getMessages().get(0).getDetails());
        }else {
            throw new ValidationException("invalid address");
        }
    }
}


