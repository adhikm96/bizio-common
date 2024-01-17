package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.SubscriptionStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "ost_subscriptions")
@NoArgsConstructor
@Setter
@Getter
public class OSTSubscription {
    @EmbeddedId
    private OSTSubscriptionPKey id;

    private UUID orgId;
    private String orgCode;
    private SubscriptionStatusEnum status;
}
