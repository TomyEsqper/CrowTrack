package com.tuplataforma.core.infrastructure.audit;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditTrailService {
    private final JpaAuditTrailRepository repo;
    public AuditTrailService(JpaAuditTrailRepository repo) { this.repo = repo; }
    @Transactional
    public void append(String userId, String requestId, String eventType, String payload, java.time.Instant occurredAt) {
        AuditTrailEvent e = new AuditTrailEvent();
        e.setUserId(userId);
        e.setRequestId(requestId);
        e.setEventType(eventType);
        e.setPayload(payload);
        e.setOccurredAt(occurredAt);
        repo.save(e);
    }
}

