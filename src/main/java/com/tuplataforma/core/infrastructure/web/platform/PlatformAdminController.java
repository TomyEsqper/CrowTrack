package com.tuplataforma.core.infrastructure.web.platform;

import com.tuplataforma.core.application.subscription.ChangePlanUseCase;
import com.tuplataforma.core.application.subscription.CancelSubscriptionUseCase;
import com.tuplataforma.core.application.subscription.ReactivateSubscriptionUseCase;
import com.tuplataforma.core.application.security.ApplicationPermissionGuard;
import com.tuplataforma.core.application.security.Permission;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/platform/admin")
public class PlatformAdminController {
    private final ChangePlanUseCase changePlanUseCase;
    private final CancelSubscriptionUseCase cancelSubscriptionUseCase;
    private final ReactivateSubscriptionUseCase reactivateSubscriptionUseCase;
    private final ApplicationPermissionGuard permissionGuard;

    public PlatformAdminController(ChangePlanUseCase changePlanUseCase, CancelSubscriptionUseCase cancelSubscriptionUseCase, ReactivateSubscriptionUseCase reactivateSubscriptionUseCase, ApplicationPermissionGuard permissionGuard) {
        this.changePlanUseCase = changePlanUseCase;
        this.cancelSubscriptionUseCase = cancelSubscriptionUseCase;
        this.reactivateSubscriptionUseCase = reactivateSubscriptionUseCase;
        this.permissionGuard = permissionGuard;
    }

    @PostMapping("/tenants/{tenantId}/plan")
    public ResponseEntity<Void> assignPlan(@PathVariable String tenantId, @RequestParam String planCode) {
        permissionGuard.require(Permission.PLATFORM_TENANT_MANAGE);
        TenantContext.setTenantId(new TenantId(tenantId));
        changePlanUseCase.upgradeNow(TenantContext.getTenantId(), planCode);
        TenantContext.clear();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tenants/{tenantId}/plan/downgrade")
    public ResponseEntity<Void> downgradePlan(@PathVariable String tenantId, @RequestParam String planCode) {
        permissionGuard.require(Permission.PLATFORM_TENANT_MANAGE);
        TenantContext.setTenantId(new TenantId(tenantId));
        changePlanUseCase.downgradeNextCycle(TenantContext.getTenantId(), planCode);
        TenantContext.clear();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tenants/{tenantId}/subscription/cancel")
    public ResponseEntity<Void> cancel(@PathVariable String tenantId) {
        permissionGuard.require(Permission.PLATFORM_TENANT_MANAGE);
        TenantContext.setTenantId(new TenantId(tenantId));
        cancelSubscriptionUseCase.cancelAtPeriodEnd(TenantContext.getTenantId());
        TenantContext.clear();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/tenants/{tenantId}/subscription/reactivate")
    public ResponseEntity<Void> reactivate(@PathVariable String tenantId) {
        permissionGuard.require(Permission.PLATFORM_TENANT_MANAGE);
        TenantContext.setTenantId(new TenantId(tenantId));
        reactivateSubscriptionUseCase.reactivate(TenantContext.getTenantId());
        TenantContext.clear();
        return ResponseEntity.ok().build();
    }
}

