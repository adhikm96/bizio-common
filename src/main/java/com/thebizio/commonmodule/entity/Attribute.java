package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.AttributeTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "attributes")
@Getter
@Setter
@NoArgsConstructor
public class Attribute extends LastUpdateDetail{
    @Id
    private String code;

    private String description;
    private AttributeTypeEnum attributeType;
}