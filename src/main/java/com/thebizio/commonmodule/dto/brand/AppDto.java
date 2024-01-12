package com.thebizio.commonmodule.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDto {
    @NotNull
    @NotBlank
    private String appCode;

    @NotNull @NotBlank
    private String brandedAppName;
    private String brandedAppDescription;

    @NotNull @NotBlank
    private String brandedUrl;
}
