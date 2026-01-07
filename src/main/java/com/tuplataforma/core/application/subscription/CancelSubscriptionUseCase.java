package com.tuplataforma.core.application.subscription;

import com.tuplataforma.core.shared.tenant.TenantId;

public interface CancelSubscriptionUseCase {
    void cancelAtPeriodEnd(TenantId tenantId);
}

