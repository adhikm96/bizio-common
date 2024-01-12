package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.ListObjConvertor;
import com.thebizio.commonmodule.dto.brand.AppDto;
import com.thebizio.commonmodule.dto.brand.SeoDto;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "brand_profiles")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BrandProfile extends LastUpdateDetail{
    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String domain;
    private String brandName;
    private String logo;
    private String favicon;
    private String loader;
    private String homeUrl;

    @Column(columnDefinition = "TEXT")
    @Convert(converter = ListObjConvertor.class)
    List<SeoDto> seo = new ArrayList<>();

    @Column(columnDefinition = "TEXT")
    @Convert(converter = ListObjConvertor.class)
    List<AppDto> apps = new ArrayList<>();
}
