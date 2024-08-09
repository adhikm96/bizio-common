package com.thebizio.commonmodule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class OrgContractPKey implements Serializable {
    private String orgCode;
    private String productCode;
    private String subName;
}
