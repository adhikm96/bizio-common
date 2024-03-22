package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import com.thebizio.commonmodule.enums.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class Order extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @GeneratorType(type = SecureRandomReferenceIdGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "ref_no", unique = true, nullable = false, updatable = false, length = 64)
    private String refNo;

    private LocalDate postingDate;

    @Positive
    private BigDecimal grossTotal;

    private BigDecimal tax;

    @Column(columnDefinition="TEXT")
    private String taxStr;

    private String promoCode;

    private BigDecimal discount;

    @Column(columnDefinition="TEXT")
    private String discountStr;

    @PositiveOrZero
    private BigDecimal netTotal;

    private OrderStatus status;

    @ManyToOne
    @JoinColumn(name = "address_id")
    @JsonBackReference
    private Address address;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @ManyToOne
    @JoinColumn(name = "product_variant_id")
    @JsonBackReference
    private ProductVariant productVariant;

    @ManyToOne
    @JoinColumn(name = "price_id")
    @JsonBackReference
    private Price price;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    @JsonBackReference
    private Promotion promotion;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    @JsonBackReference
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "parent_organization_id")
    @JsonBackReference
    private Organization parentOrganization;

    @ManyToOne
    @JoinColumn(name = "lead_id")
    @JsonBackReference
    private Lead lead;

}
