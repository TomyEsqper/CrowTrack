package com.tuplataforma.core.domain.events;

import java.time.Instant;

public interface DomainEvent {
    Instant occurredAt();
}

