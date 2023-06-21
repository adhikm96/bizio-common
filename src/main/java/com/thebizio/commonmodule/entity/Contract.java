package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.ContractEntityEnum;
import com.thebizio.commonmodule.enums.ContractStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.thebizio.commonmodule.convertor.HashMapConvertor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "contracts")
@IdClass(ContractPKey.class)
@Getter
@Setter
@NoArgsConstructor
public class Contract extends LastUpdateDetail{
    @Id
    private String entityCode;

    @Id
    private String productGroupCode;

    private ContractEntityEnum contractEntity;
    private ContractStatus status;

    @Convert(converter = HashMapConvertor.class)
    private Map<String, String> attributes = new HashMap<>();
}
