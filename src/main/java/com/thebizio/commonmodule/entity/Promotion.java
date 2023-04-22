package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "promotions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Promotion extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(updatable = false)
    private String code;

    private LocalDateTime endDate;

    @Column(updatable = false)
    private Integer maxRedemptions;

    private Integer timesRedeemed;

    private Status status;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    public boolean isValid(){
        if(!status.equals(Status.ENABLED)){
            return false;
        }

        if(endDate != null && endDate.isBefore(LocalDateTime.now())){
            return false;
        }

        if(timesRedeemed != null && (timesRedeemed >= maxRedemptions)){
            return false;
        }

        return true;
    }
}
