package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.SubscriptionStatusEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
    @Id
    private UUID subId;
    private UUID orgId;
    private String orgCode;
    private String username;
    private SubscriptionStatusEnum status;
}
