package com.tuplataforma.core.infrastructure.persistence.identity;

import com.tuplataforma.core.domain.identity.Email;
import com.tuplataforma.core.domain.identity.User;
import com.tuplataforma.core.domain.identity.ports.output.UserRepository;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = new UserEntity(user);
        return jpaUserRepository.save(entity).toDomain();
    }

    @Override
    public Optional<User> findByEmailAndTenantId(Email email, TenantId tenantId) {
        return jpaUserRepository.findByEmailAndTenantId(email.getValue(), tenantId.getValue())
            .map(UserEntity::toDomain);
    }
}
