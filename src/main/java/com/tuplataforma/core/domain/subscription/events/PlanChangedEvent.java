package com.tuplataforma.core.domain.subscription.events;

import com.tuplataforma.core.domain.events.DomainEvent;

import java.time.Instant;

public final class PlanChangedEvent implements DomainEvent {
    private final String tenantId;
    private final String newPlanCode;
    private final Instant occurredAt;
    public PlanChangedEvent(String tenantId, String newPlanCode, Instant occurredAt) {
        this.tenantId = tenantId;
        this.newPlanCode = newPlanCode;
        this.occurredAt = occurredAt;
    }
    public String getTenantId() { return tenantId; }
    public String getNewPlanCode() { return newPlanCode; }
    @Override
    public Instant occurredAt() { return occurredAt; }
}

