package com.tuplataforma.core.infrastructure.outbox;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class OutboxPublisherService {
    private static final Logger log = LoggerFactory.getLogger(OutboxPublisherService.class);

    private final JpaOutboxEventRepository outboxRepository;

    public OutboxPublisherService(JpaOutboxEventRepository outboxRepository) {
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public int publishPending() {
        List<OutboxEvent> pending = outboxRepository.findTop100ByPublishedFalseOrderByOccurredOnAsc();
        int publishedCount = 0;
        for (OutboxEvent e : pending) {
            try {
                log.info("Outbox publish: tenant={}, aggregateType={}, aggregateId={}, eventType={}, occurredOn={}",
                        e.getTenantId(), e.getAggregateType(), e.getAggregateId(), e.getEventType(), e.getOccurredOn());
                e.setPublished(true);
                e.setPublishedAt(Instant.now());
                outboxRepository.save(e);
                publishedCount++;
            } catch (Exception ex) {
                log.error("Outbox publish failed for id={}, tenant={}, error={}", e.getId(), e.getTenantId(), ex.getMessage());
            }
        }
        return publishedCount;
    }
}

