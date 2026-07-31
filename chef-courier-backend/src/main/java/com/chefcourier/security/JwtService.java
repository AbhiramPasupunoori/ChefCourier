package com.chefcourier.security;

import com.chefcourier.entity.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final String issuer;
    private final long expirationMilliseconds;

    public JwtService(
            JwtEncoder jwtEncoder,

            @Value("${app.jwt.issuer}")
            String issuer,

            @Value("${app.jwt.expiration-ms}")
            long expirationMilliseconds
    ) {
        this.jwtEncoder = jwtEncoder;
        this.issuer = issuer;
        this.expirationMilliseconds =
                expirationMilliseconds;
    }

    public String generateToken(
            User user
    ) {
        Instant issuedAt =
                Instant.now();

        JwtClaimsSet claims =
                JwtClaimsSet.builder()
                        .issuer(issuer)
                        .issuedAt(issuedAt)
                        .expiresAt(
                                issuedAt.plusMillis(
                                        expirationMilliseconds
                                )
                        )
                        .subject(user.getEmail())
                        .claim(
                                "userId",
                                user.getId()
                        )
                        .claim(
                                "name",
                                user.getFullName()
                        )
                        .claim(
                                "roles",
                                List.of(
                                        user.getRole().name()
                                )
                        )
                        .build();

        return jwtEncoder
                .encode(
                        JwtEncoderParameters.from(
                                claims
                        )
                )
                .getTokenValue();
    }

    public long getExpirationSeconds() {
        return expirationMilliseconds / 1000;
    }
}
