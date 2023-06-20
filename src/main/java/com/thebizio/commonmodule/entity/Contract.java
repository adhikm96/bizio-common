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
public class Contract{

    @Id
    private String entityCode;

    @Id
    @ManyToOne
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;

    private ContractEntityEnum contractEntity;
    private ContractStatus status;

    @Convert(converter = HashMapConvertor.class)
    private Map<String, String> attributes = new HashMap<>();
}
