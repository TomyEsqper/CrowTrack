package com.tuplataforma.core.domain.subscription.events;

import com.tuplataforma.core.domain.events.DomainEvent;

import java.time.Instant;

public final class QuotaExceededEvent implements DomainEvent {
    private final String tenantId;
    private final String type;
    private final Instant occurredAt;
    public QuotaExceededEvent(String tenantId, String type, Instant occurredAt) {
        this.tenantId = tenantId;
        this.type = type;
        this.occurredAt = occurredAt;
    }
    public String getTenantId() { return tenantId; }
    public String getType() { return type; }
    @Override
    public Instant occurredAt() { return occurredAt; }
}

