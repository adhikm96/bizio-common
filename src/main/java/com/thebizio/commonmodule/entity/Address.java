package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "addresses")
@Getter
@Setter
public class Address extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String addressLine1;
    private String addressLine2;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private Status status;

    @Column(columnDefinition = "boolean default false")
    private Boolean primaryAddress;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;

    @ManyToOne
    @JoinColumn(name = "acc_id")
    @JsonBackReference
    private Account account;

    @ManyToOne
    @JoinColumn(name = "org_id")
    @JsonBackReference
    private Organization org;
}
