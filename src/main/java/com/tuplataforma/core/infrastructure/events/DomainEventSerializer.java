package com.tuplataforma.core.infrastructure.events;

import com.tuplataforma.core.domain.events.DomainEvent;

public interface DomainEventSerializer {
    String serialize(DomainEvent event);
    String eventType(DomainEvent event);
    String version(DomainEvent event);
}

