package com.tuplataforma.core.application.subscription;

import com.tuplataforma.core.domain.subscription.Subscription;
import com.tuplataforma.core.shared.tenant.TenantId;

public interface SubscriptionProvider {
    Subscription currentFor(TenantId tenantId);
}

