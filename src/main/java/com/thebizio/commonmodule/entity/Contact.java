package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.Status;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "contacts")
@Getter
@Setter
public class Contact extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String firstName;
    private String middleName;
    private String lastName;
    private String mobile;
    private String email;
    private String fax;
    private String website;
    private Status status;

    @Column(columnDefinition = "boolean default false")
    private Boolean primaryContact;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "acc_id")
    private Account account;

    @ManyToOne
    @JoinColumn(name = "org_id")
    private Organization org;

}
