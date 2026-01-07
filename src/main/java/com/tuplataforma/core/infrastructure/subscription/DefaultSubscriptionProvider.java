package com.tuplataforma.core.infrastructure.subscription;

import com.tuplataforma.core.application.subscription.SubscriptionProvider;
import com.tuplataforma.core.domain.subscription.Plan;
import com.tuplataforma.core.domain.subscription.PlanCode;
import com.tuplataforma.core.domain.subscription.Subscription;
import com.tuplataforma.core.domain.subscription.SubscriptionStatus;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DefaultSubscriptionProvider implements SubscriptionProvider {
    private final JpaSubscriptionRepository repo;
    public DefaultSubscriptionProvider(JpaSubscriptionRepository repo) { this.repo = repo; }
    @Override
    public Subscription currentFor(TenantId tenantId) {
        var e = repo.findTop1ByTenantIdOrderByRenewalDateDesc(tenantId.getValue()).orElseGet(() -> {
            SubscriptionEntity se = new SubscriptionEntity();
            se.setPlanCode("FREE");
            se.setStatus("ACTIVE");
            se.setStartDate(java.time.LocalDate.now());
            se.setRenewalDate(java.time.LocalDate.now().plusMonths(1));
            return se;
        });
        PlanCode code = PlanCode.valueOf(e.getPlanCode());
        Plan plan = defaultPlan(code);
        SubscriptionStatus st = SubscriptionStatus.valueOf(e.getStatus());
        return new Subscription(tenantId.getValue(), plan, st, e.getStartDate(), e.getRenewalDate());
    }
    private Plan defaultPlan(PlanCode code) {
        return switch (code) {
            case FREE -> new Plan(PlanCode.FREE, Set.of("VEHICLE","FLEET"), 5, 2, 5, 100, 0);
            case BASIC -> new Plan(PlanCode.BASIC, Set.of("VEHICLE","FLEET"), 50, 10, 50, 5000, 1999);
            case PRO -> new Plan(PlanCode.PRO, Set.of("VEHICLE","FLEET"), 500, 100, 500, 50000, 9999);
            case ENTERPRISE -> new Plan(PlanCode.ENTERPRISE, Set.of("VEHICLE","FLEET"), Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, 0);
        };
    }
}

