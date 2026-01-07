package com.tuplataforma.core.domain.identity.ports.output;

import com.tuplataforma.core.domain.identity.PasswordHash;

public interface PasswordHasher {
    PasswordHash hash(String password);
    boolean matches(String password, PasswordHash hash);
}
