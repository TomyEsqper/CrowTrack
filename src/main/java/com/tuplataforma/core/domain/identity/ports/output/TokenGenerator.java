package com.tuplataforma.core.domain.identity.ports.output;

import com.tuplataforma.core.domain.identity.User;

public interface TokenGenerator {
    String generateToken(User user);
}
