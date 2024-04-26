package com.thebizio.commonmodule.dto.domain;

import com.thebizio.commonmodule.enums.DnsRecordType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDnsRecordDto {
    private MxRecordDto mx;
    private CnameRecordDto cname;
    private TxtRecordDto txt;
    private SrvRecordDto srv;
}
