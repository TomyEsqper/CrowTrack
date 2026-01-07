package com.tuplataforma.core.infrastructure.finance;

import com.tuplataforma.core.domain.subscription.events.PlanChangedEvent;
import com.tuplataforma.core.domain.subscription.events.QuotaExceededEvent;
import com.tuplataforma.core.domain.subscription.events.SubscriptionCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class FinanceAuditListener {
    private final JpaFinanceAuditRepository repo;
    public FinanceAuditListener(JpaFinanceAuditRepository repo) { this.repo = repo; }
    @EventListener
    public void on(SubscriptionCreatedEvent e) {
        FinanceAuditRecord r = new FinanceAuditRecord();
        r.setEventType("SubscriptionCreated");
        r.setPayload("{\"plan\":\""+e.getPlanCode()+"\"}");
        r.setOccurredAt(e.occurredAt());
        repo.save(r);
    }
    @EventListener
    public void on(PlanChangedEvent e) {
        FinanceAuditRecord r = new FinanceAuditRecord();
        r.setEventType("PlanChanged");
        r.setPayload("{\"newPlan\":\""+e.getNewPlanCode()+"\"}");
        r.setOccurredAt(e.occurredAt());
        repo.save(r);
    }
    @EventListener
    public void on(QuotaExceededEvent e) {
        FinanceAuditRecord r = new FinanceAuditRecord();
        r.setEventType("QuotaExceeded");
        r.setPayload("{\"type\":\""+e.getType()+"\"}");
        r.setOccurredAt(e.occurredAt());
        repo.save(r);
    }
}

