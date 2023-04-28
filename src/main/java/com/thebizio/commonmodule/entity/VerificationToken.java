package com.thebizio.commonmodule.entity;

import com.thebizio.commonmodule.enums.TokenType;
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

    @Email
    @Column(nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private LocalDateTime expiryDate;

    private TokenType tokenType;

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
