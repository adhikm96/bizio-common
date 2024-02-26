package com.thebizio.commonmodule.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "events")
@Getter
@Setter
@NoArgsConstructor
public class Event {
    @Id
    private String eventKey; // {projCode}|{moduleCode}|{EventCode}

    private String code;
    private String name;
    private String encryptedKey;
    private String encryptedKeyName;

    @ManyToOne
    @JoinColumn(name = "module_id")
    private Module module;
}
