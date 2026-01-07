package com.tuplataforma.core.infrastructure.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaAuditTrailRepository extends JpaRepository<AuditTrailEvent, UUID> {
}

