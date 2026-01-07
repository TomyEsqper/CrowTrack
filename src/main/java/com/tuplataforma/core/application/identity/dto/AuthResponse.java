package com.tuplataforma.core.application.identity.dto;

import com.tuplataforma.core.domain.identity.Role;
import java.util.Set;

public record AuthResponse(
    String token,
    String userId,
    String email,
    Set<Role> roles
) {}
