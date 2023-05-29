package com.thebizio.commonmodule.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
public class Role extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String code;

    @Column(columnDefinition="TEXT")
    private String description;

    private Status status;

    @ManyToMany
    @JoinTable(
            name = "role_policies",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "policy_id"))
    @JsonBackReference
    private List<Policy> policies = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonBackReference
    private Project project;

}
