package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.AssignmentResult;
import com.tuplataforma.core.application.events.DomainEventPublisher;
import com.tuplataforma.core.application.governance.Module;
import com.tuplataforma.core.application.governance.TenantPolicyGuard;
import com.tuplataforma.core.application.idempotency.IdempotencyContext;
import com.tuplataforma.core.application.security.ApplicationPermissionGuard;
import com.tuplataforma.core.application.security.Permission;
import com.tuplataforma.core.application.subscription.SubscriptionGuard;
import com.tuplataforma.core.application.usage.UsageTracker;
import com.tuplataforma.core.application.performance.PerformanceMonitor;
import com.tuplataforma.core.domain.fleet.Fleet;
import com.tuplataforma.core.domain.fleet.FleetRepository;
import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.domain.fleet.VehicleRepository;
import com.tuplataforma.core.domain.fleet.ports.input.AssignVehicleToFleetUseCase;
import com.tuplataforma.core.infrastructure.idempotency.IdempotencyRecord;
import com.tuplataforma.core.infrastructure.idempotency.JpaIdempotencyRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssignVehicleToFleetService implements AssignVehicleToFleetUseCase {

    private final VehicleRepository vehicleRepository;
    private final FleetRepository fleetRepository;
    private final DomainEventPublisher eventPublisher;
    private final TenantPolicyGuard tenantPolicyGuard;
    private final ApplicationPermissionGuard permissionGuard;
    private final JpaIdempotencyRepository idempotencyRepository;
    private final SubscriptionGuard subscriptionGuard;
    private final UsageTracker usageTracker;
    private final PerformanceMonitor performanceMonitor;

    public AssignVehicleToFleetService(VehicleRepository vehicleRepository, FleetRepository fleetRepository, DomainEventPublisher eventPublisher, TenantPolicyGuard tenantPolicyGuard, ApplicationPermissionGuard permissionGuard, JpaIdempotencyRepository idempotencyRepository, SubscriptionGuard subscriptionGuard, UsageTracker usageTracker, PerformanceMonitor performanceMonitor) {
        this.vehicleRepository = vehicleRepository;
        this.fleetRepository = fleetRepository;
        this.eventPublisher = eventPublisher;
        this.tenantPolicyGuard = tenantPolicyGuard;
        this.permissionGuard = permissionGuard;
        this.idempotencyRepository = idempotencyRepository;
        this.subscriptionGuard = subscriptionGuard;
        this.usageTracker = usageTracker;
        this.performanceMonitor = performanceMonitor;
    }

    @Transactional
    @Override
    public AssignmentResult execute(AssignVehicleToFleetCommand command) {
        long t0 = System.nanoTime();
        tenantPolicyGuard.ensureActiveAndModuleEnabled(Module.VEHICLE);
        tenantPolicyGuard.ensureActiveAndModuleEnabled(Module.FLEET);
        subscriptionGuard.ensureActive();
        subscriptionGuard.ensureModule("VEHICLE");
        subscriptionGuard.ensureModule("FLEET");
        permissionGuard.require(Permission.VEHICLE_ASSIGN);
        String idemKey = IdempotencyContext.get();
        if (idemKey != null) {
            var opt = idempotencyRepository.findByTenantIdAndIdempotencyKeyAndUseCase(TenantContext.getTenantId().getValue(), idemKey, "AssignVehicleToFleet");
            if (opt.isPresent()) {
                return deserializeAssignmentResult(opt.get().getResultPayload());
            }
        }
        Vehicle vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new com.tuplataforma.core.domain.fleet.exceptions.VehicleNotFoundException(command.vehicleId()));
        Fleet fleet = fleetRepository.findById(command.fleetId())
                .orElseThrow(() -> new com.tuplataforma.core.domain.fleet.exceptions.FleetNotFoundException(command.fleetId()));
        vehicle.assignTo(fleet);
        var events = new java.util.ArrayList<>(vehicle.collectDomainEvents());
        Vehicle saved = vehicleRepository.save(vehicle);
        eventPublisher.publishAfterCommit(events);
        vehicle.clearDomainEvents();
        AssignmentResult result = new AssignmentResult(saved.getId(), saved.getAssignedFleetId());
        usageTracker.apiCall("VEHICLE");
        if (idemKey != null) {
            IdempotencyRecord r = new IdempotencyRecord();
            r.setUseCase("AssignVehicleToFleet");
            r.setIdempotencyKey(idemKey);
            r.setResultPayload(serializeAssignmentResult(result));
            idempotencyRepository.save(r);
        }
        long latencyMs = (System.nanoTime() - t0) / 1_000_000;
        performanceMonitor.record("AssignVehicleToFleet", latencyMs);
        return result;
    }

    private String serializeAssignmentResult(AssignmentResult r) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(r);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            throw new IllegalStateException(e);
        }
    }
    private AssignmentResult deserializeAssignmentResult(String s) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().readValue(s, AssignmentResult.class);
        } catch (java.io.IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
