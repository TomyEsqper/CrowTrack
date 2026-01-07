package com.tuplataforma.core.application.subscription;

import com.tuplataforma.core.application.governance.QuotaExceededException;
import com.tuplataforma.core.infrastructure.usage.JpaUsageMetricRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class SubscriptionGuard {
    private final SubscriptionProvider provider;
    private final JpaUsageMetricRepository usageRepo;
    public SubscriptionGuard(SubscriptionProvider provider, JpaUsageMetricRepository usageRepo) {
        this.provider = provider;
        this.usageRepo = usageRepo;
    }
    public void ensureActive() {
        var sub = provider.currentFor(TenantContext.getTenantId());
        if (!sub.isActive()) throw new PlanRestrictionException();
    }
    public void ensureModule(String module) {
        var sub = provider.currentFor(TenantContext.getTenantId());
        if (!sub.canUseModule(module)) throw new PlanRestrictionException();
    }
    public void ensureVehicleQuota() {
        var sub = provider.currentFor(TenantContext.getTenantId());
        String tenant = TenantContext.getTenantId().getValue();
        String period = LocalDate.now().withDayOfMonth(1).toString();
        long count = usageRepo.findByTenantIdAndPeriodAndType(tenant, period, "vehicles_created").map(m -> m.getCount()).orElse(0L);
        if (count >= sub.getPlan().vehiclesLimit()) throw new QuotaExceededException();
    }
    public void ensureFleetQuota() {
        var sub = provider.currentFor(TenantContext.getTenantId());
        String tenant = TenantContext.getTenantId().getValue();
        String period = LocalDate.now().withDayOfMonth(1).toString();
        long count = usageRepo.findByTenantIdAndPeriodAndType(tenant, period, "fleets_created").map(m -> m.getCount()).orElse(0L);
        if (count >= sub.getPlan().fleetsLimit()) throw new QuotaExceededException();
    }
}

