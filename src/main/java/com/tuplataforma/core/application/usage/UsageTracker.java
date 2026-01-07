package com.tuplataforma.core.application.usage;

import com.tuplataforma.core.infrastructure.usage.JpaUsageMetricRepository;
import com.tuplataforma.core.infrastructure.usage.UsageMetric;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
public class UsageTracker {
    private final JpaUsageMetricRepository repo;
    public UsageTracker(JpaUsageMetricRepository repo) { this.repo = repo; }
    @Transactional
    public void increment(String type) {
        String tenant = TenantContext.getTenantId().getValue();
        String period = LocalDate.now().withDayOfMonth(1).toString();
        var opt = repo.findByTenantIdAndPeriodAndType(tenant, period, type);
        UsageMetric m = opt.orElseGet(() -> {
            UsageMetric nm = new UsageMetric();
            nm.setPeriod(period);
            nm.setType(type);
            nm.setCount(0);
            return nm;
        });
        m.setCount(m.getCount() + 1);
        repo.save(m);
    }
    public void vehiclesCreated() { increment("vehicles_created"); }
    public void fleetsCreated() { increment("fleets_created"); }
    public void apiCall(String module) { increment("api_calls_" + module); }
}

