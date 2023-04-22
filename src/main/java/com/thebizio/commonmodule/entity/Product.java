package com.thebizio.commonmodule.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.ProductType;
import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
public class Product extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String code;
    private Status status;
    private ProductType type;

    private Boolean bundle;

    private Boolean plan;

    private Boolean subscribable;

    @Column(columnDefinition="TEXT")
    private String planInfo;

    private String stripeProductId;

    @ManyToOne
    @JoinColumn(name = "product_group_id")
    @JsonBackReference
    private ProductGroup productGroup;
}
