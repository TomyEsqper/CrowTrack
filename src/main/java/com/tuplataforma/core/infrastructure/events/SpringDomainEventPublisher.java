package com.tuplataforma.core.infrastructure.events;

import com.tuplataforma.core.application.events.DomainEventPublisher;
import com.tuplataforma.core.domain.events.DomainEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publishAfterCommit(List<? extends DomainEvent> events) {
        if (events == null || events.isEmpty()) return;
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    events.forEach(applicationEventPublisher::publishEvent);
                }
            });
        } else {
            events.forEach(applicationEventPublisher::publishEvent);
        }
    }
}

