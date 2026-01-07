package com.tuplataforma.core.infrastructure.persistence.repository;

import com.tuplataforma.core.infrastructure.persistence.entity.TenantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaTenantRepository extends JpaRepository<TenantEntity, String> {
}
