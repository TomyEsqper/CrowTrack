package com.tuplataforma.core.infrastructure.performance;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "performance_metrics", indexes = {
        @Index(name = "idx_perf_tenant_period_uc", columnList = "tenant_id, period, use_case", unique = true)
})
public class PerformanceMetric extends BaseTenantEntity {
    @Column(name = "period", nullable = false)
    private String period;
    @Column(name = "use_case", nullable = false)
    private String useCase;
    @Column(name = "count", nullable = false)
    private long count;
    @Column(name = "total_latency_ms", nullable = false)
    private long totalLatencyMs;
    @Column(name = "max_latency_ms", nullable = false)
    private long maxLatencyMs;
    public String getPeriod() { return period; }
    public String getUseCase() { return useCase; }
    public long getCount() { return count; }
    public long getTotalLatencyMs() { return totalLatencyMs; }
    public long getMaxLatencyMs() { return maxLatencyMs; }
    public void setPeriod(String period) { this.period = period; }
    public void setUseCase(String useCase) { this.useCase = useCase; }
    public void setCount(long count) { this.count = count; }
    public void setTotalLatencyMs(long totalLatencyMs) { this.totalLatencyMs = totalLatencyMs; }
    public void setMaxLatencyMs(long maxLatencyMs) { this.maxLatencyMs = maxLatencyMs; }
}

