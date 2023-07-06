package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.AttributeTypeEnum;
import com.thebizio.commonmodule.enums.MergeTypeEnum;
import com.thebizio.commonmodule.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
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

    private Status status;

    @Column(columnDefinition = "integer default 0")
    private MergeTypeEnum mergeType;
}