package com.thebizio.commonmodule.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PolicyResourceScopeAttrDto {
    private String resourceCode;
    private String policyCode;
    private String scopeCode;
    private Map<String, String> attributes = new HashMap<>();
}
