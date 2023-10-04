package com.thebizio.commonmodule.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.thebizio.commonmodule.enums.ContractMigrationStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@Table(name = "contract_migrations")
@Getter
@Setter
public class ContractMigration extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private ContractMigrationStatus status;

    @OneToMany(mappedBy="contractMigration")
    @JsonBackReference
    private List<ContractChangeRequest> changeRequests  = new ArrayList<>();
}
