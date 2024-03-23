package com.thebizio.commonmodule.dto.lead;

import com.thebizio.commonmodule.enums.GenderEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalDetails {

    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String firstName;

    @NotNull(message = "must not be null or blank")
    @NotBlank(message = "must not be null or blank")
    private String lastName;

    private GenderEnum gender;

    private LocalDate dob;

    private String jobTitle;

    @Email
    private String workEmail;
    private String phoneNumber;
}
