package com.tuplataforma.core.infrastructure.billing;

import com.tuplataforma.core.application.billing.BillingProvider;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockBillingProvider implements BillingProvider {
    private static final Logger log = LoggerFactory.getLogger(MockBillingProvider.class);
    @Override
    public void recordSubscriptionCreated(TenantId tenantId, String planCode) {
        log.info("billing_event type=subscription_created tenant={} plan={}", tenantId.getValue(), planCode);
    }
    @Override
    public void recordPlanChanged(TenantId tenantId, String newPlanCode) {
        log.info("billing_event type=plan_changed tenant={} plan={}", tenantId.getValue(), newPlanCode);
    }
    @Override
    public void recordQuotaExceeded(TenantId tenantId, String type) {
        log.info("billing_event type=quota_exceeded tenant={} metric={}", tenantId.getValue(), type);
    }
}

