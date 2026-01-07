package com.tuplataforma.core.infrastructure.outbox;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "outbox_events", indexes = {
        @Index(name = "idx_outbox_tenant_published", columnList = "tenant_id, published")
})
public class OutboxEvent extends BaseTenantEntity {

    @Column(name = "aggregate_type", nullable = false)
    private String aggregateType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "payload", nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(name = "occurred_on", nullable = false)
    private Instant occurredOn;

    @Column(name = "published", nullable = false)
    private boolean published = false;

    @Column(name = "published_at")
    private Instant publishedAt;

    public String getAggregateType() { return aggregateType; }
    public UUID getAggregateId() { return aggregateId; }
    public String getEventType() { return eventType; }
    public String getPayload() { return payload; }
    public Instant getOccurredOn() { return occurredOn; }
    public boolean isPublished() { return published; }
    public Instant getPublishedAt() { return publishedAt; }

    public void setAggregateType(String aggregateType) { this.aggregateType = aggregateType; }
    public void setAggregateId(UUID aggregateId) { this.aggregateId = aggregateId; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setPayload(String payload) { this.payload = payload; }
    public void setOccurredOn(Instant occurredOn) { this.occurredOn = occurredOn; }
    public void setPublished(boolean published) { this.published = published; }
    public void setPublishedAt(Instant publishedAt) { this.publishedAt = publishedAt; }
}

