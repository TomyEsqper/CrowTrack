package com.tuplataforma.core.application.governance;

import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import com.tuplataforma.core.shared.tenant.TenantStatus;
import org.springframework.stereotype.Component;

@Component
public class TenantPolicyGuard {

    private final TenantPolicyProvider provider;

    public TenantPolicyGuard(TenantPolicyProvider provider) {
        this.provider = provider;
    }

    public void ensureActiveAndModuleEnabled(Module module) {
        TenantId tenantId = TenantContext.getTenantId();
        TenantPolicy policy = provider.policyFor(tenantId);
        if (policy.status() != TenantStatus.ACTIVE) throw new TenantSuspendedException();
        if (!policy.enabledModules().contains(module)) throw new ModuleDisabledException();
    }

    public void ensureVehicleQuota() {
        TenantId tenantId = TenantContext.getTenantId();
        TenantPolicy p = provider.policyFor(tenantId);
        if (provider.currentVehicleCount(tenantId) >= p.vehicleLimit()) throw new QuotaExceededException();
    }

    public void ensureFleetQuota() {
        TenantId tenantId = TenantContext.getTenantId();
        TenantPolicy p = provider.policyFor(tenantId);
        if (provider.currentFleetCount(tenantId) >= p.fleetLimit()) throw new QuotaExceededException();
    }
}

