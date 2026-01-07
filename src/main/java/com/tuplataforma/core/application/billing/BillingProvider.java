package com.tuplataforma.core.application.billing;

import com.tuplataforma.core.shared.tenant.TenantId;

public interface BillingProvider {
    void recordSubscriptionCreated(TenantId tenantId, String planCode);
    void recordPlanChanged(TenantId tenantId, String newPlanCode);
    void recordQuotaExceeded(TenantId tenantId, String type);
}

