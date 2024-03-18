package com.thebizio.commonmodule.entity;

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
    private String lastName;
    private GenderEnum gender;
    private LocalDate dob;
    private String designation;

    @Column(updatable = false)
    private String spaceId;

    @Column(updatable = false)
    private UUID oidcId;

    @Column(columnDefinition = "boolean default false")
    private Boolean twoFA;

    private String avatar;
    private Status status;

    private LocalDateTime lastEmailChangeDate;
    private LocalDateTime lastPasswordChangeDate;

    @Column(columnDefinition = "boolean default false")
    private Boolean stayInformedAboutBizio;

    @Column(columnDefinition = "boolean default false")
    private Boolean termsConditionsAgreed;

    private LocalDateTime termsConditionsAgreedTimestamp;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contact contact;

}
