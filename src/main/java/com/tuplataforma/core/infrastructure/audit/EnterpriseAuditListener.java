package com.tuplataforma.core.infrastructure.audit;

import com.tuplataforma.core.domain.audit.SecurityAuditEvent;
import com.tuplataforma.core.domain.subscription.events.PlanChangedEvent;
import com.tuplataforma.core.domain.subscription.events.SubscriptionCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EnterpriseAuditListener {
    private final AuditTrailService trail;
    public EnterpriseAuditListener(AuditTrailService trail) { this.trail = trail; }
    @EventListener
    public void on(SecurityAuditEvent e) {
        trail.append(e.getUserId(), e.getRequestId(), e.getType(), "{}", e.occurredAt());
    }
    @EventListener
    public void on(SubscriptionCreatedEvent e) {
        trail.append("system", "n/a", "SubscriptionCreated", "{\"plan\":\""+e.getPlanCode()+"\"}", e.occurredAt());
    }
    @EventListener
    public void on(PlanChangedEvent e) {
        trail.append("system", "n/a", "PlanChanged", "{\"newPlan\":\""+e.getNewPlanCode()+"\"}", e.occurredAt());
    }
}

