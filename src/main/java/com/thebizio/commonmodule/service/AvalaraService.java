package com.thebizio.commonmodule.service;

import com.thebizio.commonmodule.dto.BillingAddress;
import com.thebizio.commonmodule.entity.ProductVariant;
import com.thebizio.commonmodule.exception.ValidationException;
import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.TransactionBuilder;
import net.avalara.avatax.rest.client.enums.DocumentType;
import net.avalara.avatax.rest.client.enums.TextCase;
import net.avalara.avatax.rest.client.enums.TransactionAddressType;
import net.avalara.avatax.rest.client.models.AddressInfo;
import net.avalara.avatax.rest.client.models.AddressResolutionModel;
import net.avalara.avatax.rest.client.models.TransactionModel;
import net.avalara.avatax.rest.client.models.ValidatedAddressInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
//        AddressResolutionModel arm = addressValidate(ba);
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTotalTax(BigDecimal.valueOf(0));
        transactionModel.setSummary(new ArrayList<>());
        return transactionModel;

//        return new TransactionBuilder(
//                    avaTaxClient,
//                    avalaraCompanyCode,
//                    dt,
//                    orgCode
//                )
//                .withAddress(
//                        TransactionAddressType.SingleLocation,
//                        arm.getValidatedAddresses().get(0).getLine1(),
//                        arm.getValidatedAddresses().get(0).getLine2(),
//                        null,
//                        arm.getValidatedAddresses().get(0).getCity(),
//                        arm.getValidatedAddresses().get(0).getRegion(),
//                        arm.getValidatedAddresses().get(0).getPostalCode(),
//                        COUNTRY
//                )
//                .withLine(
//                        productWithDiscount ,
//                        quantity,
//                        avalaraTaxCode,
//                        productVariant.getProduct().getCode(),
//                        productVariant.getProduct().getName()
//                )
//                .Create();
    }

    public TransactionModel createTransactionTaxInclusive(BillingAddress ba, ProductVariant productVariant, String orgCode, DocumentType dt, BigDecimal netTotal) throws Exception {
        AddressResolutionModel arm =addressValidate(ba);
        TransactionBuilder transaction = new TransactionBuilder(avaTaxClient, avalaraCompanyCode, dt, orgCode)
                .withAddress(TransactionAddressType.SingleLocation, arm.getValidatedAddresses().get(0).getLine1(),
                        arm.getValidatedAddresses().get(0).getLine2(), null, arm.getValidatedAddresses().get(0).getCity(),arm.getValidatedAddresses().get(0).getRegion(),
                        arm.getValidatedAddresses().get(0).getPostalCode(), COUNTRY)
                .withLine( netTotal, quantity, avalaraTaxCode,
                        productVariant.getProduct().getCode(),productVariant.getProduct().getName());

        transaction.getIntermediaryTransactionModel().getLines().forEach(model -> model.setTaxIncluded(true));
        return transaction.Create();
    }

    public AddressResolutionModel addressValidate(BillingAddress ba) throws Exception {

//        AddressResolutionModel arm = avaTaxClient.resolveAddress(
//                ba.getAddressLine1(),
//                ba.getAddressLine2(),
//                null,
//                ba.getCity(),
//                ba.getState(),
//                ba.getZipcode(),
//                COUNTRY,
//                TextCase.Mixed
//        );

        AddressInfo addressInfo = new AddressInfo();
        addressInfo.setLine1(ba.getAddressLine1());
        addressInfo.setLine2(ba.getAddressLine2());
        addressInfo.setCity(ba.getCity());
        addressInfo.setRegion(ba.getState());
        addressInfo.setPostalCode(ba.getZipcode());
        addressInfo.setCountry(COUNTRY);

        ValidatedAddressInfo validatedAddressInfo = new ValidatedAddressInfo();
        validatedAddressInfo.setLine1(ba.getAddressLine1());
        validatedAddressInfo.setLine2(ba.getAddressLine2());
        validatedAddressInfo.setCity(ba.getCity());
        validatedAddressInfo.setRegion(ba.getState());
        validatedAddressInfo.setPostalCode(ba.getZipcode());
        validatedAddressInfo.setCountry(COUNTRY);

        ArrayList<ValidatedAddressInfo> validatedAddressInfoList = new ArrayList<>();
        validatedAddressInfoList.add(validatedAddressInfo);

        AddressResolutionModel arm = new AddressResolutionModel();
        arm.setAddress(addressInfo);
        arm.setValidatedAddresses(validatedAddressInfoList);

        return arm;

//        String errorMsg = "invalid address";
//
//        if (arm.getValidatedAddresses().size() == 0) throw new ValidationException(errorMsg);
//
//        String addressType =  arm.getValidatedAddresses().get(0).getAddressType();
//
//        if(addressType == null) throw new ValidationException(errorMsg);
//
//        if(!addressType.equals("UnknownAddressType")) return arm;
//
//
//        if(arm.getMessages() == null || arm.getMessages().size() == 0) throw new ValidationException(errorMsg);
//
//        if (!arm.getMessages().get(0).getSummary().isEmpty())
//            errorMsg = arm.getMessages().get(0).getSummary();
//        else if (!arm.getMessages().get(0).getDetails().isEmpty())
//            errorMsg = arm.getMessages().get(0).getDetails();
//
//        throw new ValidationException(errorMsg);
    }
}


