package com.thebizio.commonmodule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class UContractResolvedRSAKey implements Serializable {
    private String policyCode;
    private String resourceCode;
    private String scopeCode;
}
