package com.thebizio.commonmodule.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Getter
@Setter
public class UserPreference extends LastUpdateDetail{

    @Id
    @GeneratedValue(generator = "uuid4")
    @Column(columnDefinition = "uuid")
    private UUID id;

    private String timeZone;
    private String language;
    private String countryLocale;
    private String dateFormat;
    private String timeFormat;
    private String numberFormat;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
