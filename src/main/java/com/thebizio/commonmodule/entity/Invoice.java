package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.InvoiceStatus;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import lombok.AllArgsConstructor;
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
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Invoice extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @GeneratorType(type = SecureRandomReferenceIdGenerator.class, when = GenerationTime.INSERT)
    @Column(name = "ref_no", unique = true, nullable = false, updatable = false, length = 64)
    private String refNo;

    private LocalDate postingDate;
    private String promoCode;
    private BigDecimal discount;
    private InvoiceStatus status;

    @Column(columnDefinition = "TEXT")
    private String discountStr;

    @Column(columnDefinition = "TEXT")
    private String taxes;

    @Positive
    private BigDecimal grossTotal;

    @PositiveOrZero
    private BigDecimal netTotal;

    //set payment

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @ManyToOne
    @JoinColumn(name = "subscription_id")
    private Subscription subscription;
}