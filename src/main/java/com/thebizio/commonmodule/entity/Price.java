package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "prices")
@Data
@NoArgsConstructor
public class Price extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private Double price;
    private Boolean isDefault;
    private Status status;
    private String associatedFunction;


    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    @JsonBackReference
    private ProductVariant productVariant;

}
