package com.tuplataforma.core.infrastructure.persistence.company;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface JpaCompanyRepository extends JpaRepository<CompanyEntity, UUID> {
    Optional<CompanyEntity> findByIdAndTenantId(UUID id, String tenantId);
}
