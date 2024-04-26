package com.thebizio.commonmodule.dto.domain;

import com.thebizio.commonmodule.enums.DnsRecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SrvRecordDto {

    private String name;
    private Integer ttl;
    private String cname;
    private DnsRecordType type;
    private Integer priority;
    private Integer weight;
    private Integer port;
    private String value;

}
