package com.tuplataforma.core.application.subscription;

import com.tuplataforma.core.shared.tenant.TenantId;

public interface ReactivateSubscriptionUseCase {
    void reactivate(TenantId tenantId);
}

