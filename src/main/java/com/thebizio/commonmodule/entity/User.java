package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.thebizio.commonmodule.enums.GenderEnum;
import com.thebizio.commonmodule.enums.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity @Table(name = "users") @Getter @Setter
public class User extends LastUpdateDetail {
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(updatable = false)
    private String username;

    private String email;

    private String firstName;
    private String middleName;
    private String lastName;
    private GenderEnum gender;
    private LocalDate dob;

    @Column(updatable = false)
    private String spaceId;

    @Column(updatable = false)
    private UUID oidcId;

    @Column(columnDefinition = "boolean default false")
    private Boolean twoFA;

    private String avatar;
    private Status status;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime lastEmailChangeDate;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime lastPasswordChangeDate;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
