package com.tuplataforma.core.infrastructure.security;

import com.tuplataforma.core.domain.identity.ports.output.PasswordHasher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.tuplataforma.core.domain.identity.PasswordHash;

@Component
public class BCryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public PasswordHash hash(String password) {
        return new PasswordHash(encoder.encode(password));
    }

    @Override
    public boolean matches(String password, PasswordHash hash) {
        return encoder.matches(password, hash.getValue());
    }
}
