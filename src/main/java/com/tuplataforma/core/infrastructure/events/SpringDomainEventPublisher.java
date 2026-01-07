package com.tuplataforma.core.infrastructure.events;

import com.tuplataforma.core.application.events.DomainEventPublisher;
import com.tuplataforma.core.domain.events.DomainEvent;
import com.tuplataforma.core.infrastructure.outbox.JpaOutboxEventRepository;
import com.tuplataforma.core.infrastructure.outbox.OutboxEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final JpaOutboxEventRepository outboxRepository;
    private final DomainEventSerializer serializer;

    public SpringDomainEventPublisher(JpaOutboxEventRepository outboxRepository, DomainEventSerializer serializer) {
        this.outboxRepository = outboxRepository;
        this.serializer = serializer;
    }

    @Override
    public void publishAfterCommit(List<? extends DomainEvent> events) {
        if (events == null || events.isEmpty()) return;
        for (DomainEvent event : events) {
            OutboxEvent out = new OutboxEvent();
            out.setAggregateType(resolveAggregateType(event));
            out.setAggregateId(resolveAggregateId(event));
            out.setEventType(serializer.eventType(event));
            out.setPayload(serializer.serialize(event));
            out.setOccurredOn(event.occurredAt());
            outboxRepository.save(out);
        }
    }

    private String resolveAggregateType(DomainEvent event) {
        if (event.getClass().getSimpleName().contains("Vehicle")) return "Vehicle";
        if (event.getClass().getSimpleName().contains("Fleet")) return "Fleet";
        return "Unknown";
    }

    private java.util.UUID resolveAggregateId(DomainEvent event) {
        try {
            var m = event.getClass().getMethod("getVehicleId");
            Object id = m.invoke(event);
            if (id instanceof java.util.UUID uuid) return uuid;
        } catch (Exception ignored) {}
        try {
            var m = event.getClass().getMethod("getAggregateId");
            Object id = m.invoke(event);
            if (id instanceof java.util.UUID uuid) return uuid;
        } catch (Exception ignored) {}
        return null;
    }
}

