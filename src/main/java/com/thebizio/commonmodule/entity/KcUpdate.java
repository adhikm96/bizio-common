package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.AccType;
import com.thebizio.commonmodule.enums.Status;
import com.thebizio.commonmodule.enums.TaskStatus;
import com.thebizio.commonmodule.generator.SecureRandomReferenceIdGenerator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenerationTime;
import org.hibernate.annotations.GeneratorType;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;
}
