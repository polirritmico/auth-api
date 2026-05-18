/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.auth.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private static final String ROLE = "roles";

    @Value("${jwt.expiration}")
    private Long expiration;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String user, String role) {
        Instant now = Instant.now();
        return JWT.create()
                .withSubject(user)
                .withIssuer(issuer)
                .withClaim(ROLE, role)
                .withIssuedAt(now)
                .withExpiresAt(now.plusMillis(expiration))
                .sign(Algorithm.HMAC256(secret));
    }
}
