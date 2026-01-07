package com.tuplataforma.core.application.subscription;

import com.tuplataforma.core.shared.tenant.TenantId;

public interface ChangePlanUseCase {
    void upgradeNow(TenantId tenantId, String planCode);
    void downgradeNextCycle(TenantId tenantId, String planCode);
}

