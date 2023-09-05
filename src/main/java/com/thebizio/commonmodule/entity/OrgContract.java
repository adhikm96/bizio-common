package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.HashMapConvertor;
import com.thebizio.commonmodule.enums.ContractStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "org_contracts")
@IdClass(OrgContractPKey.class)
@Getter
@Setter
@NoArgsConstructor
public class OrgContract extends LastUpdateDetail{
    @Id
    private String orgCode;

    @Id
    private String productCode;

    private ContractStatus status;

    @Convert(converter = HashMapConvertor.class)
    private Map<String, String> attributes = new HashMap<>();
}
