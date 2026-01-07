package com.tuplataforma.core.infrastructure.flags;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaFeatureFlagRepository extends JpaRepository<FeatureFlagEntity, UUID> {
    Optional<FeatureFlagEntity> findTop1ByTenantIdAndFlagKeyAndEnvironmentOrderByCreatedAtDesc(String tenantId, String flagKey, String environment);
}

