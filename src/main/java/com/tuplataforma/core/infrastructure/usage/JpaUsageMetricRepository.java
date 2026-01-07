package com.tuplataforma.core.infrastructure.usage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaUsageMetricRepository extends JpaRepository<UsageMetric, UUID> {
    Optional<UsageMetric> findByTenantIdAndPeriodAndType(String tenantId, String period, String type);
}

