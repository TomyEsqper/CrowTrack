package com.tuplataforma.core.application.subscription;

import com.tuplataforma.core.infrastructure.subscription.JpaSubscriptionRepository;
import com.tuplataforma.core.infrastructure.subscription.SubscriptionEntity;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class SubscriptionLifecycleService implements ChangePlanUseCase, CancelSubscriptionUseCase, ReactivateSubscriptionUseCase {
    private final JpaSubscriptionRepository repo;
    public SubscriptionLifecycleService(JpaSubscriptionRepository repo) { this.repo = repo; }
    @Transactional
    @Override
    public void upgradeNow(TenantId tenantId, String planCode) {
        var e = repo.findTop1ByTenantIdOrderByRenewalDateDesc(tenantId.getValue()).orElseGet(SubscriptionEntity::new);
        e.setPlanCode(planCode);
        e.setStatus("ACTIVE");
        if (e.getStartDate() == null) e.setStartDate(LocalDate.now());
        e.setRenewalDate(LocalDate.now().plusMonths(1));
        repo.save(e);
    }
    @Transactional
    @Override
    public void downgradeNextCycle(TenantId tenantId, String planCode) {
        var e = repo.findTop1ByTenantIdOrderByRenewalDateDesc(tenantId.getValue()).orElseGet(SubscriptionEntity::new);
        e.setPlanCode(planCode);
        if (e.getStartDate() == null) e.setStartDate(LocalDate.now());
        if (e.getRenewalDate() == null) e.setRenewalDate(LocalDate.now().plusMonths(1));
        repo.save(e);
    }
    @Transactional
    @Override
    public void cancelAtPeriodEnd(TenantId tenantId) {
        var e = repo.findTop1ByTenantIdOrderByRenewalDateDesc(tenantId.getValue()).orElseGet(SubscriptionEntity::new);
        e.setStatus("CANCELED");
        if (e.getStartDate() == null) e.setStartDate(LocalDate.now());
        if (e.getRenewalDate() == null) e.setRenewalDate(LocalDate.now().plusMonths(1));
        repo.save(e);
    }
    @Transactional
    @Override
    public void reactivate(TenantId tenantId) {
        var e = repo.findTop1ByTenantIdOrderByRenewalDateDesc(tenantId.getValue()).orElseGet(SubscriptionEntity::new);
        e.setStatus("ACTIVE");
        if (e.getStartDate() == null) e.setStartDate(LocalDate.now());
        if (e.getRenewalDate() == null) e.setRenewalDate(LocalDate.now().plusMonths(1));
        repo.save(e);
    }
}

