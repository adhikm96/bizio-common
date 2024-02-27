package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.KcUpdateTypeEnum;
import com.thebizio.commonmodule.enums.Status;
import com.thebizio.commonmodule.enums.TaskStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity @NoArgsConstructor @Table(name = "kc_updates")
@Getter @Setter
public class KcUpdate extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private Status requiredAction;

    private TaskStatus status;

    @Column(columnDefinition = "integer default 0")
    private KcUpdateTypeEnum updateType;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
