package com.tuplataforma.core.infrastructure.security.jwt;

import com.tuplataforma.core.domain.identity.Role;
import com.tuplataforma.core.domain.identity.User;
import com.tuplataforma.core.domain.identity.ports.output.TokenGenerator;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements TokenGenerator {

    private final SecretKey key;
    private final long expirationTime;

    public JwtTokenProvider(
        @Value("${jwt.secret:default-secret-key-must-be-very-long-and-secure-at-least-256-bits}") String secret,
        @Value("${jwt.expiration:3600000}") long expirationTime
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationTime = expirationTime;
    }

    @Override
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationTime);

        return Jwts.builder()
            .subject(user.getId().toString())
            .claim("tenantId", user.getTenantId().getValue())
            .claim("roles", user.getRoles().stream().map(Enum::name).collect(Collectors.toList()))
            .issuedAt(now)
            .expiration(expiryDate)
            .signWith(key)
            .compact();
    }

    public Claims validateToken(String token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
