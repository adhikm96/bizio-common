package com.thebizio.commonmodule.dto.tax;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TaxAddress {
    @SerializedName("country")
    String country;
    @SerializedName("zip")
    String zip;
    @SerializedName("state")
    String state;
    @SerializedName("city")
    String city;
    @SerializedName("street")
    String street;
}
