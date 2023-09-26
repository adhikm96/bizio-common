package com.thebizio.commonmodule.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@NoArgsConstructor
@Entity
@Table(name = "contact_us")
public class ContactUs extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    private String firstName;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    private String lastName;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    private String jobTitle;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    @Email
    private String workEmail;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    private String phone;

    private String company;

    private String employees;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    private String country;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    private String productInterest;

    @NotNull(message = "cannot be null or empty")
    @NotBlank(message = "cannot be null or empty")
    private String questionsComments;
}
