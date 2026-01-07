package com.tuplataforma.core.application.identity.dto;

import com.tuplataforma.core.domain.identity.Role;
import java.util.Set;

public record RegisterUserCommand(
    String email,
    String password,
    Set<Role> roles
) {}
