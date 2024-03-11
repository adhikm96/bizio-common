package com.thebizio.commonmodule.dto.tax;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class TaxResp {
    Double tax;
    String summary;
}
