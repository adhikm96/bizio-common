package com.thebizio.commonmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor@NoArgsConstructor
public class OrderResponseDto {

    private String stripeCustomerId;
    private String orderRefNo;
    private String productName;
    private String productCode;
    private String attributeValue;

    private List<AddOnsDto> addons;
    private BigDecimal subTotal;
    private BigDecimal tax;
    private String taxStr;
    private BigDecimal discount;
    private String discountStr;
    private BigDecimal netTotal;

}
