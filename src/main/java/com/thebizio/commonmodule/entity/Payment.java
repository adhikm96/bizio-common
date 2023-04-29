package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.PaymentStatus;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "payments")
public class Payment extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @GeneratorType(type = SecureRandomReferenceIdGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "ref_no", unique = true, nullable = false, updatable = false, length = 64)
    private String refNo;

    private LocalDate paymentDate;
    private BigDecimal amount;
    private String paymentRef;
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "billing_account_id")
    @JsonBackReference
    private BillingAccount billingAccount;


}