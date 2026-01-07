package com.tuplataforma.core.domain.subscription;

import java.time.LocalDate;

public class Subscription {
    private final String tenantId;
    private final Plan plan;
    private final SubscriptionStatus status;
    private final LocalDate startDate;
    private final LocalDate renewalDate;

    public Subscription(String tenantId, Plan plan, SubscriptionStatus status, LocalDate startDate, LocalDate renewalDate) {
        this.tenantId = tenantId;
        this.plan = plan;
        this.status = status;
        this.startDate = startDate;
        this.renewalDate = renewalDate;
    }

    public String getTenantId() { return tenantId; }
    public Plan getPlan() { return plan; }
    public SubscriptionStatus getStatus() { return status; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getRenewalDate() { return renewalDate; }
    public boolean isActive() { return status == SubscriptionStatus.ACTIVE; }
    public boolean canUseModule(String module) { return plan.includesModule(module); }
}

