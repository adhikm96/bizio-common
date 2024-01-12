package com.thebizio.commonmodule.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SeoDto {

    @NotNull @NotBlank
    String tag;

    @NotNull @NotBlank
    String value;
}
