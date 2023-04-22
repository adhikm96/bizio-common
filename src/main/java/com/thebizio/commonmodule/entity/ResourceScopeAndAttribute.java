package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.convertor.HashMapConvertor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
public class ResourceScopeAndAttribute {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "resource_id")
    @JsonBackReference
    private Resource resource;

    @ManyToOne
    @JoinColumn(name = "policy_id", nullable = true)
    @JsonBackReference
    private Policy policy;

    @ManyToOne
    @JoinColumn(name = "scope_id", nullable = true)
    @JsonBackReference
    private Scope scope;

    @Convert(converter = HashMapConvertor.class)
    private Map<String, String> attributes = new HashMap<>();
}
