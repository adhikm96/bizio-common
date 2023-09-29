package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.ChangeRequestStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "contract_change_requests")
@Getter
@Setter
public class ContractChangeRequest extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(columnDefinition="TEXT")
    private String payload;

    @Column(columnDefinition="TEXT")
    private String oldRecord;

    @Column(columnDefinition="TEXT")
    private String newRecord;

    private ChangeRequestStatus status;

    private String entityId;

    private String entityName;

    private Boolean changeRequested;
    private String payloadAction;
}
