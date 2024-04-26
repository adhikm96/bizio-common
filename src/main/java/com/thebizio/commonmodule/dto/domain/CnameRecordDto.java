package com.thebizio.commonmodule.dto.domain;

import com.thebizio.commonmodule.enums.DnsRecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CnameRecordDto {

    private String name;
    private Integer ttl;
    private String cname;
    private DnsRecordType type;
    private String value;

}
