package com.tuplataforma.core.infrastructure.performance;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaPerformanceMetricRepository extends JpaRepository<PerformanceMetric, UUID> {
    Optional<PerformanceMetric> findByTenantIdAndPeriodAndUseCase(String tenantId, String period, String useCase);
}

