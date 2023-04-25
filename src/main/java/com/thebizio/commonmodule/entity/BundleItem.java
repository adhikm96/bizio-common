package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Table(name = "bundle_items", uniqueConstraints = {@UniqueConstraint(columnNames = {"product_variant_id","bundle_item_id"})})
public class BundleItem {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    @JsonBackReference
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "bundle_item_id")
//    @JsonBackReference
    private ProductVariant bundleItem;

    private Double priceAllocation;
}
