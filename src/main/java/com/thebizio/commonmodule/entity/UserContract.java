package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.ListConvertorForPolicyResourceScopeAttrDto;
import com.thebizio.commonmodule.dto.PolicyResourceScopeAttrDto;
import com.thebizio.commonmodule.enums.ContractStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "user_contracts")
@IdClass(UserContractPKey.class)
@Getter
@Setter
@NoArgsConstructor
public class UserContract extends LastUpdateDetail {

    @Id
    private String email;

    @Id
    private String appCode;

    @Id
    private String orgCode;

    private ContractStatus status;

    // to be commented later after migration
    @Column(columnDefinition = "TEXT")
    @Convert(converter = ListConvertorForPolicyResourceScopeAttrDto.class)

    private List<PolicyResourceScopeAttrDto> resolvedResourceScopeAttrs = new ArrayList<>();


    @Column(columnDefinition = "uuid", name = "resolved_rsa_key", unique = true)
//    @NaturalId - not working now bcz of old entries
    private UUID resolvedRSAKey;

    @OneToMany(mappedBy = "userContract", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<UContractResolvedRSA> resolvedRSAList = new ArrayList<>();
}
