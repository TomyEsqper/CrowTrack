package com.tuplataforma.core.domain.identity.ports.output;

import com.tuplataforma.core.domain.identity.Email;
import com.tuplataforma.core.domain.identity.User;
import com.tuplataforma.core.shared.tenant.TenantId;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    Optional<User> findByEmailAndTenantId(Email email, TenantId tenantId);
}
