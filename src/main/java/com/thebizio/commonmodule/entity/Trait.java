package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.TraitTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "traits")
@Getter
@Setter
@NoArgsConstructor
public class Trait {
    @Id
    private String code;

    private String description;
    private TraitTypeEnum traitType;
}