package com.tuplataforma.core.domain.subscription.events;

import com.tuplataforma.core.domain.events.DomainEvent;

import java.time.Instant;

public final class SubscriptionCreatedEvent implements DomainEvent {
    private final String tenantId;
    private final String planCode;
    private final Instant occurredAt;
    public SubscriptionCreatedEvent(String tenantId, String planCode, Instant occurredAt) {
        this.tenantId = tenantId;
        this.planCode = planCode;
        this.occurredAt = occurredAt;
    }
    public String getTenantId() { return tenantId; }
    public String getPlanCode() { return planCode; }
    @Override
    public Instant occurredAt() { return occurredAt; }
}

