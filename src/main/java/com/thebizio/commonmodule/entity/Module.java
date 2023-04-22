package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Table(name = "modules")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class Module extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(nullable = false)
    @NotNull(message = "Module name is required")
    private String name;

    @Column(nullable = false, updatable = false)
    private String code;

    @Column(columnDefinition="TEXT")
    private String description;

    private Status status;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    @NotNull(message = "Project is required")
    private Project project;
}