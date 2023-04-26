package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "projects")
@Data
@NoArgsConstructor
public class Project extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String code;

    @Column(columnDefinition="TEXT")
    private String description;
    private Status status;

}
