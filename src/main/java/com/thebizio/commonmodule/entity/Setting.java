package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.ListConvertor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "settings")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Setting extends LastUpdateDetail{
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(columnDefinition = "TEXT", name = "bizio_center_cors_urls")
    @Convert(converter = ListConvertor.class)
    private List<String> bizioCenterCORSUrls = new ArrayList<>();

    @Column(columnDefinition = "TEXT", name = "auth_whitelisted_urls")
    @Convert(converter = ListConvertor.class)
    private List<String> authWhitelistedUrls = new ArrayList<>();

    private Integer personalThreshold;
    private Integer businessThreshold;
}
