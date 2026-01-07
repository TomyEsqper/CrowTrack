package com.tuplataforma.core.infrastructure.idempotency;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaIdempotencyRepository extends JpaRepository<IdempotencyRecord, UUID> {
    Optional<IdempotencyRecord> findByTenantIdAndIdempotencyKeyAndUseCase(String tenantId, String idempotencyKey, String useCase);
}

