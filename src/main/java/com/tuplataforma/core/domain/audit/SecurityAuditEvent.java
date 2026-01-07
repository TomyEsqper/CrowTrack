package com.tuplataforma.core.domain.audit;

import com.tuplataforma.core.domain.events.DomainEvent;

import java.time.Instant;

public final class SecurityAuditEvent implements DomainEvent {
    private final String tenantId;
    private final String userId;
    private final String requestId;
    private final String type;
    private final Instant occurredAt;
    public SecurityAuditEvent(String tenantId, String userId, String requestId, String type, Instant occurredAt) {
        this.tenantId = tenantId;
        this.userId = userId;
        this.requestId = requestId;
        this.type = type;
        this.occurredAt = occurredAt;
    }
    public String getTenantId() { return tenantId; }
    public String getUserId() { return userId; }
    public String getRequestId() { return requestId; }
    public String getType() { return type; }
    @Override
    public Instant occurredAt() { return occurredAt; }
}

