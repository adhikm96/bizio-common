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
@Table(name = "resources")
@Data
@NoArgsConstructor
public class Resource extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String code;

    @Column(columnDefinition="TEXT")
    private String description;
    private Status status;

    @ManyToOne
    @JoinColumn(name = "module_id")
    @JsonBackReference
    private Module module;

    @ManyToMany
    @JoinTable(
            name = "resource_scopes",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "scope_id"))
    private List<Scope> scopes = new ArrayList<>();
}
