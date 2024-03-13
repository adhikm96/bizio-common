package com.thebizio.commonmodule.dto.tax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@ToString
public class TaxResp {
    BigDecimal tax;
    String summary;
}
