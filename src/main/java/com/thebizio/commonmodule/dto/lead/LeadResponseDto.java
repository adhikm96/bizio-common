package com.thebizio.commonmodule.dto.lead;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeadResponseDto {

    private UUID leadId;
}
