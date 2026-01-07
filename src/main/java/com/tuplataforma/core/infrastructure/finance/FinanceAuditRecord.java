package com.tuplataforma.core.infrastructure.finance;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "finance_audit")
public class FinanceAuditRecord extends BaseTenantEntity {
    @Column(name = "event_type", nullable = false)
    private String eventType;
    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;
    @Column(name = "occurred_at", nullable = false)
    private java.time.Instant occurredAt;
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public java.time.Instant getOccurredAt() { return occurredAt; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setOccurredAt(java.time.Instant occurredAt) { this.occurredAt = occurredAt; }
}

