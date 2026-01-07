package com.tuplataforma.core.domain.identity.ports.input;

import com.tuplataforma.core.application.identity.dto.AuthResponse;
import com.tuplataforma.core.application.identity.dto.LoginCommand;

public interface AuthenticateUserUseCase {
    AuthResponse authenticate(LoginCommand command);
}
