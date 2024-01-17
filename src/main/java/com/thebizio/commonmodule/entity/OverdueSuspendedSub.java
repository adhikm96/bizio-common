package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.SubscriptionStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table(name = "overdue_suspended_subs")
@NoArgsConstructor
@Setter
@Getter
public class OverdueSuspendedSub {
    @EmbeddedId
    private OverdueSuspendedSubPKey id;

    private UUID orgId;
    private String orgCode;
    private SubscriptionStatusEnum status;
}
