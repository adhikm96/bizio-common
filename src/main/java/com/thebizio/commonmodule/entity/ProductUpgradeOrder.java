package com.thebizio.commonmodule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "product_upgrade_orders")
@Setter
@Getter
@NoArgsConstructor
public class ProductUpgradeOrder {

    @EmbeddedId
    private ProductUpgradeOrderKey id;

    private Integer number;
}