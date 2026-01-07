package com.tuplataforma.core.infrastructure.audit;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "audit_trail")
public class AuditTrailEvent extends BaseTenantEntity {
    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;
    @Column(name = "request_id", nullable = false, updatable = false)
    private String requestId;
    @Column(name = "event_type", nullable = false, updatable = false)
    private String eventType;
    @Column(name = "payload", nullable = false, updatable = false, columnDefinition = "TEXT")
    private String payload;
    @Column(name = "occurred_at", nullable = false, updatable = false)
    private java.time.Instant occurredAt;
    public String getUserId() { return userId; }
    public String getRequestId() { return requestId; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public java.time.Instant getOccurredAt() { return occurredAt; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setRequestId(String requestId) { this.requestId = requestId; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setOccurredAt(java.time.Instant occurredAt) { this.occurredAt = occurredAt; }
}

