package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.convertor.UUIDListConvertor;
import com.thebizio.commonmodule.enums.AttributeType;
import com.thebizio.commonmodule.enums.PlanTypeEnum;
import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "product_variants")
@Data
@NoArgsConstructor
public class ProductVariant extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String code;
    private Status status;
    private AttributeType attributeType;
    private String attributeValue;
    private Double defaultPrice;

    private Integer seats;
    private PlanTypeEnum planType;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "application_id")
    @JsonBackReference
    private Application application;

    @ManyToMany
    @JoinTable(
            name = "product_variant_policies",
            joinColumns = @JoinColumn(name = "product_variant_id"),
            inverseJoinColumns = @JoinColumn(name = "policy_id"))
    @JsonBackReference
    private List<Policy> policies = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_variant_roles",
            joinColumns = @JoinColumn(name = "product_variant_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    @JsonBackReference
    private List<Role> roles = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "product_variant_add_ons",
            joinColumns = @JoinColumn(name = "product_variant_id"),
            inverseJoinColumns = @JoinColumn(name = "add_on_id"))
    @JsonBackReference
    private List<ProductVariant> addOns = new ArrayList<>();


    @OneToMany(mappedBy="productVariant")
    @JsonBackReference
    private List<BundleItem> bundleItems = new ArrayList<>();

    @Convert(converter = UUIDListConvertor.class)
    private List<UUID> extensionOf = new ArrayList<>();

    @Column(columnDefinition = "boolean default false")
    private Boolean addonExtension;
}
