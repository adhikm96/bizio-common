package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "actions")
@Getter
@Setter
@NoArgsConstructor
public class Action extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String code;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Status status;
}
