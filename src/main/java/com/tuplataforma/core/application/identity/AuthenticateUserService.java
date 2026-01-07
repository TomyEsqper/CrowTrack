package com.tuplataforma.core.application.identity;

import com.tuplataforma.core.application.identity.dto.AuthResponse;
import com.tuplataforma.core.application.identity.dto.LoginCommand;
import com.tuplataforma.core.domain.identity.Email;
import com.tuplataforma.core.domain.identity.User;
import com.tuplataforma.core.domain.identity.ports.input.AuthenticateUserUseCase;
import com.tuplataforma.core.domain.identity.ports.output.PasswordHasher;
import com.tuplataforma.core.domain.identity.ports.output.TokenGenerator;
import com.tuplataforma.core.domain.identity.ports.output.UserRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class AuthenticateUserService implements AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public AuthenticateUserService(UserRepository userRepository, PasswordHasher passwordHasher, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthResponse authenticate(LoginCommand command) {
        TenantId tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context is required for authentication");
        }

        Email email = new Email(command.email());
        
        User user = userRepository.findByEmailAndTenantId(email, tenantId)
            .orElseThrow(() -> new IllegalArgumentException("Invalid credentials"));

        if (!passwordHasher.matches(command.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!user.isActive()) {
            throw new IllegalStateException("User is inactive");
        }

        String token = tokenGenerator.generateToken(user);

        return new AuthResponse(
            token,
            user.getId().toString(),
            user.getEmail().getValue(),
            user.getRoles()
        );
    }
}
