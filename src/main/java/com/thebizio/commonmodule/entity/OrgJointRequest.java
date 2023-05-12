package com.thebizio.commonmodule.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.OrgJoinReqStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "org_join_requests")
@Getter
@Setter
public class OrgJointRequest {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate dob;
    private String email;
    private String country;
    private String state;
    private String city;
    private OrgJoinReqStatus status;
    private String requestedEmailDomain;

    @ManyToOne
    @JoinColumn(name = "org_id")
    @JsonBackReference
    private Organization organization;
}
