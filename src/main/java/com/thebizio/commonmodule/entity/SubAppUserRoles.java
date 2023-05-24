package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "sub_app_user_roles")
public class SubAppUserRoles extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "sub_id")
    @JsonBackReference
    private Subscription sub;

    @ManyToOne
    @JoinColumn(name = "app_id")
    @JsonBackReference
    private Application app;

    @ManyToOne
    @JoinColumn(name = "role_id")
    @JsonBackReference
    private Role role;
}
