package com.thebizio.commonmodule.dto;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor@NoArgsConstructor
public class OrderResponseDto {

    private String stripeCustomerId;
    private String clientSecretKey;
    private String orderRefNo;
    private String productName;
    private String productCode;
    private String attributeValue;

    private List<AddOnsDto> addons;
    private BigDecimal price;
    private BigDecimal grossTotal;
    private BigDecimal tax;
    private JsonNode taxStr;
    private BigDecimal discount;
    private JsonNode discountStr;
    private BigDecimal netTotal;

}
