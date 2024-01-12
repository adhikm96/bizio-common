package com.thebizio.commonmodule.dto.brand;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppDto {
    private String appCode;
    private String brandedAppName;
    private String brandedAppDescription;
    private String brandedUrl;
}
