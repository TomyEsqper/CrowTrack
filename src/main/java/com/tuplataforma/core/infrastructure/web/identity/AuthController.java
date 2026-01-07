package com.tuplataforma.core.infrastructure.web.identity;

import com.tuplataforma.core.application.identity.dto.AuthResponse;
import com.tuplataforma.core.application.identity.dto.LoginCommand;
import com.tuplataforma.core.application.identity.dto.RegisterUserCommand;
import com.tuplataforma.core.domain.identity.ports.input.AuthenticateUserUseCase;
import com.tuplataforma.core.domain.identity.ports.input.RegisterUserUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final RegisterUserUseCase registerUserUseCase;

    public AuthController(AuthenticateUserUseCase authenticateUserUseCase, RegisterUserUseCase registerUserUseCase) {
        this.authenticateUserUseCase = authenticateUserUseCase;
        this.registerUserUseCase = registerUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginCommand command) {
        return ResponseEntity.ok(authenticateUserUseCase.authenticate(command));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterUserCommand command) {
        return ResponseEntity.status(HttpStatus.CREATED).body(registerUserUseCase.register(command));
    }
}
