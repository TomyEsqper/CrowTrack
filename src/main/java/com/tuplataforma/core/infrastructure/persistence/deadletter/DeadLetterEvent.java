package com.tuplataforma.core.infrastructure.persistence.deadletter;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "dead_letter_events")
public class DeadLetterEvent extends BaseTenantEntity {

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "correlation_id", nullable = false)
    private String correlationId;

    @Column(name = "error_message", nullable = false)
    private String errorMessage;

    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public String getCorrelationId() { return correlationId; }
    public String getErrorMessage() { return errorMessage; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}

