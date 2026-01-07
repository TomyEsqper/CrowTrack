package com.tuplataforma.core.infrastructure.finance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaFinanceAuditRepository extends JpaRepository<FinanceAuditRecord, UUID> {
}

