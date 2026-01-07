package com.tuplataforma.core.application.events;

import com.tuplataforma.core.domain.events.DomainEvent;

import java.util.List;

public interface DomainEventPublisher {
    void publishAfterCommit(List<? extends DomainEvent> events);
}

