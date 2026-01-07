package com.tuplataforma.core.application.governance;

import com.tuplataforma.core.shared.tenant.TenantId;

public interface TenantPolicyProvider {
    TenantPolicy policyFor(TenantId tenantId);
    int currentVehicleCount(TenantId tenantId);
    int currentFleetCount(TenantId tenantId);
    int currentUserCount(TenantId tenantId);
}

