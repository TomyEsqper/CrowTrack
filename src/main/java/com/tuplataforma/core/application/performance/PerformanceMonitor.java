package com.tuplataforma.core.application.performance;

import com.tuplataforma.core.infrastructure.performance.JpaPerformanceMetricRepository;
import com.tuplataforma.core.infrastructure.performance.PerformanceMetric;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class PerformanceMonitor {
    private final JpaPerformanceMetricRepository repo;
    public PerformanceMonitor(JpaPerformanceMetricRepository repo) { this.repo = repo; }
    @Transactional
    public void record(String useCase, long latencyMs) {
        String tenant = TenantContext.getTenantId().getValue();
        String period = LocalDate.now().withDayOfMonth(1).toString();
        var opt = repo.findByTenantIdAndPeriodAndUseCase(tenant, period, useCase);
        PerformanceMetric m = opt.orElseGet(() -> {
            PerformanceMetric nm = new PerformanceMetric();
            nm.setPeriod(period);
            nm.setUseCase(useCase);
            nm.setCount(0);
            nm.setTotalLatencyMs(0);
            nm.setMaxLatencyMs(0);
            return nm;
        });
        m.setCount(m.getCount() + 1);
        m.setTotalLatencyMs(m.getTotalLatencyMs() + latencyMs);
        if (latencyMs > m.getMaxLatencyMs()) m.setMaxLatencyMs(latencyMs);
        repo.save(m);
    }
}

