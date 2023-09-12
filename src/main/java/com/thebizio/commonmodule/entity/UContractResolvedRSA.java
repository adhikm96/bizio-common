package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.HashMapConvertor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "user_contract_resolved_rsa")
@Data
@Getter
@Setter
@NoArgsConstructor
public class UContractResolvedRSA extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String policyCode;

    private String resourceCode;

    private String scopeCode;

    @Convert(converter = HashMapConvertor.class)
    private Map<String, String> attributes = new HashMap<>();

    @ManyToOne
    @JoinColumn(columnDefinition = "uuid", name = "resolved_rsa_key", referencedColumnName = "resolved_rsa_key", nullable = false)
    private UserContract userContract;
}
