package com.thebizio.commonmodule.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "order_payloads")
@Data
@NoArgsConstructor
public class OrderPayload {
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private String payloadType;

    @Column(columnDefinition="TEXT")
    private String payload;

    private String stripeCustomerId;
}
