package com.thebizio.commonmodule.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProductUpgradeOrderKey implements Serializable {

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "pg_id")
    private ProductGroup pg;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductUpgradeOrderKey that = (ProductUpgradeOrderKey) o;
        return product.equals(that.product) && pg.equals(that.pg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, pg);
    }
}
