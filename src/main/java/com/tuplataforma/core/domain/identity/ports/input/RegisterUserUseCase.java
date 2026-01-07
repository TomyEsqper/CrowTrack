package com.tuplataforma.core.domain.identity.ports.input;

import com.tuplataforma.core.application.identity.dto.AuthResponse;
import com.tuplataforma.core.application.identity.dto.RegisterUserCommand;

public interface RegisterUserUseCase {
    AuthResponse register(RegisterUserCommand command);
}
