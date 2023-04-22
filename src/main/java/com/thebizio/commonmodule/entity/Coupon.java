package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.thebizio.commonmodule.enums.CouponType;
import com.thebizio.commonmodule.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coupon extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;

    private CouponType type;

    @Column(updatable = false)
    private float value;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Column(updatable = false)
    private Integer maxRedemptions;

    private Status status;

    @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "coupon")
    private List<Promotion> promotions;
}
