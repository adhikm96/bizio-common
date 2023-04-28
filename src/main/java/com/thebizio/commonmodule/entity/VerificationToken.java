package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.convertor.ListObjConvertor;
import com.thebizio.commonmodule.dto.RegisterAccountPublicDto;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.time.LocalDateTime;

@Data
@Entity
public class VerificationToken extends LastUpdateDetail{

    // this expiry date is default to UID
    private static final int EXPIRATION = 60 * 24; // 1 days
//	private static final int EXPIRATION = 1; // for testing 1 min

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

//	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
//	@JoinColumn(nullable = false, name = "user_id")
//	private User user;

    @Email
    @Column(nullable = false)
    private String email;

    @Convert(converter = ListObjConvertor.class)
    @Column(length = 500)
    private RegisterAccountPublicDto regDto;

    private LocalDateTime expiryDate;

    private String tokenType;

    private String payloadClass;

    public VerificationToken() {
        expiryDate = calculateExpiryDate();
    }

    private LocalDateTime calculateExpiryDate() {
        return LocalDateTime.now().plusMinutes(EXPIRATION);
    }

    public static int getExpiration() {
        return EXPIRATION;
    }
}
