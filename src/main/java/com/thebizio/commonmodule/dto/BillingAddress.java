package com.thebizio.commonmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillingAddress {
    private String firstName;
    private String lastName;
    private String streetAddress;
    private String state;
    private String city;
    private String zip;
}
