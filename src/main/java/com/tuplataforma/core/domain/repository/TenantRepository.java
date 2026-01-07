package com.tuplataforma.core.domain.repository;

import com.tuplataforma.core.domain.model.Tenant;
import java.util.Optional;

public interface TenantRepository {
    Tenant save(Tenant tenant);
    Optional<Tenant> findById(String id);
    boolean existsById(String id);
}
