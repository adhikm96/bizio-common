package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.Status;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "product_groups")
@Data
@NoArgsConstructor
public class ProductGroup extends LastUpdateDetail {

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String name;
    private String code;
    private Status status;
}
