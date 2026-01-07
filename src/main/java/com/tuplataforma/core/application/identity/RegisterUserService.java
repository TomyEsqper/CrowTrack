package com.tuplataforma.core.application.identity;

import com.tuplataforma.core.application.identity.dto.AuthResponse;
import com.tuplataforma.core.application.identity.dto.RegisterUserCommand;
import com.tuplataforma.core.domain.identity.Email;
import com.tuplataforma.core.domain.identity.PasswordHash;
import com.tuplataforma.core.domain.identity.User;
import com.tuplataforma.core.domain.identity.ports.input.RegisterUserUseCase;
import com.tuplataforma.core.domain.identity.ports.output.PasswordHasher;
import com.tuplataforma.core.domain.identity.ports.output.TokenGenerator;
import com.tuplataforma.core.domain.identity.ports.output.UserRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public RegisterUserService(UserRepository userRepository, PasswordHasher passwordHasher, TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Override
    public AuthResponse register(RegisterUserCommand command) {
        TenantId tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            throw new IllegalStateException("Tenant context is required for registration");
        }

        Email email = new Email(command.email());

        if (userRepository.findByEmailAndTenantId(email, tenantId).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        PasswordHash passwordHash = passwordHasher.hash(command.password());
        
        User user = User.create(
            tenantId,
            email,
            passwordHash,
            command.roles()
        );

        userRepository.save(user);

        String token = tokenGenerator.generateToken(user);

        return new AuthResponse(
            token,
            user.getId().toString(),
            user.getEmail().getValue(),
            user.getRoles()
        );
    }
}
