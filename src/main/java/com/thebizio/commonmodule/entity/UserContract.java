package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.ListConvertorForPolicyResourceScopeAttrDto;
import com.thebizio.commonmodule.convertor.ListObjConvertor;
import com.thebizio.commonmodule.enums.ContractStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_contracts")
@IdClass(UserContractPKey.class)
@Getter
@Setter
@NoArgsConstructor
public class UserContract {

    @Id
    private String email;

    @Id
    private String appCode;

    @Id
    private String orgCode;

    private ContractStatus status;
    @Column(columnDefinition = "TEXT")
    @Convert(converter = ListConvertorForPolicyResourceScopeAttrDto.class)

    private List<PolicyResourceScopeAttrDto> resolvedResourceScopeAttrs = new ArrayList<>();
}
