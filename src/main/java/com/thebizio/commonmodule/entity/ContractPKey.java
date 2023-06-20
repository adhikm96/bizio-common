package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.ContractEntityEnum;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
public class ContractPKey implements Serializable {

    private String entityCode;

    @ManyToOne
    @JoinColumn(name = "product_group_id")
    private ProductGroup productGroup;
}
