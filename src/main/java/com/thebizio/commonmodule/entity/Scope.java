package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "scopes")
@Data
@NoArgsConstructor
public class Scope extends LastUpdateDetail{

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
            name = "scope_actions",
            joinColumns = @JoinColumn(name = "scope_id"),
            inverseJoinColumns = @JoinColumn(name = "action_id"))
    @JsonBackReference
    private List<Action> actions = new ArrayList<>();


    @ManyToMany(mappedBy = "scopes")
    @JsonBackReference
    Set<Resource> resources;
}
