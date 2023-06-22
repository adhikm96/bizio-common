package com.thebizio.commonmodule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class UserContractPKey implements Serializable {
    private String email;
    private String appCode;
}
