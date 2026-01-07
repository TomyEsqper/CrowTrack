package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.application.fleet.dto.VehicleResult;
import com.tuplataforma.core.application.governance.Module;
import com.tuplataforma.core.application.governance.TenantPolicyGuard;
import com.tuplataforma.core.application.idempotency.IdempotencyContext;
import com.tuplataforma.core.application.security.ApplicationPermissionGuard;
import com.tuplataforma.core.application.security.Permission;
import com.tuplataforma.core.application.subscription.SubscriptionGuard;
import com.tuplataforma.core.application.usage.UsageTracker;
import com.tuplataforma.core.application.performance.PerformanceMonitor;
import com.tuplataforma.core.application.flags.FeatureFlagEvaluator;
import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.domain.fleet.VehicleRepository;
import com.tuplataforma.core.domain.fleet.ports.input.CreateVehicleUseCase;
import com.tuplataforma.core.infrastructure.idempotency.IdempotencyRecord;
import com.tuplataforma.core.infrastructure.idempotency.JpaIdempotencyRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateVehicleService implements CreateVehicleUseCase {

    private final VehicleRepository vehicleRepository;
    private final TenantPolicyGuard tenantPolicyGuard;
    private final ApplicationPermissionGuard permissionGuard;
    private final JpaIdempotencyRepository idempotencyRepository;
    private final SubscriptionGuard subscriptionGuard;
    private final UsageTracker usageTracker;
    private final PerformanceMonitor performanceMonitor;
    private final FeatureFlagEvaluator featureFlagEvaluator;

    public CreateVehicleService(VehicleRepository vehicleRepository, TenantPolicyGuard tenantPolicyGuard, ApplicationPermissionGuard permissionGuard, JpaIdempotencyRepository idempotencyRepository, SubscriptionGuard subscriptionGuard, UsageTracker usageTracker, PerformanceMonitor performanceMonitor, FeatureFlagEvaluator featureFlagEvaluator) {
        this.vehicleRepository = vehicleRepository;
        this.tenantPolicyGuard = tenantPolicyGuard;
        this.permissionGuard = permissionGuard;
        this.idempotencyRepository = idempotencyRepository;
        this.subscriptionGuard = subscriptionGuard;
        this.usageTracker = usageTracker;
        this.performanceMonitor = performanceMonitor;
        this.featureFlagEvaluator = featureFlagEvaluator;
    }

    @Transactional
    @Override
    public VehicleResult execute(CreateVehicleCommand command) {
        long t0 = System.nanoTime();
        tenantPolicyGuard.ensureActiveAndModuleEnabled(Module.VEHICLE);
        tenantPolicyGuard.ensureVehicleQuota();
        if (!featureFlagEvaluator.isEnabled("module.vehicle")) throw new com.tuplataforma.core.application.subscription.PlanRestrictionException();
        subscriptionGuard.ensureActive();
        subscriptionGuard.ensureModule("VEHICLE");
        subscriptionGuard.ensureVehicleQuota();
        permissionGuard.require(Permission.VEHICLE_CREATE);
        String idemKey = IdempotencyContext.get();
        if (idemKey != null) {
            var opt = idempotencyRepository.findByTenantIdAndIdempotencyKeyAndUseCase(TenantContext.getTenantId().getValue(), idemKey, "CreateVehicle");
            if (opt.isPresent()) {
                IdempotencyRecord r = opt.get();
                return deserializeVehicleResult(r.getResultPayload());
            }
        }
        Vehicle vehicle = Vehicle.create(
                TenantContext.getTenantId(),
                command.licensePlate(),
                command.model()
        );
        Vehicle saved = vehicleRepository.save(vehicle);
        VehicleResult result = new VehicleResult(saved.getId(), saved.getLicensePlate(), saved.getModel(), saved.isActive());
        usageTracker.vehiclesCreated();
        usageTracker.apiCall("VEHICLE");
        if (idemKey != null) {
            IdempotencyRecord r = new IdempotencyRecord();
            r.setUseCase("CreateVehicle");
            r.setIdempotencyKey(idemKey);
            r.setResultPayload(serializeVehicleResult(result));
            idempotencyRepository.save(r);
        }
        long latencyMs = (System.nanoTime() - t0) / 1_000_000;
        performanceMonitor.record("CreateVehicle", latencyMs);
        return result;
    }

    private String serializeVehicleResult(VehicleResult r) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(r);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
    private VehicleResult deserializeVehicleResult(String s) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(s, VehicleResult.class);
        } catch (java.io.IOException e) {
            throw new IllegalStateException(e);
        }
    }
}

