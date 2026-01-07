package com.tuplataforma.core.domain.events;

import java.time.Instant;
import java.util.UUID;

public final class VehicleAssignedToFleetEvent implements DomainEvent {
    private final UUID vehicleId;
    private final UUID fleetId;
    private final String tenantId;
    private final Instant occurredAt;

    public VehicleAssignedToFleetEvent(UUID vehicleId, UUID fleetId, String tenantId, Instant occurredAt) {
        this.vehicleId = vehicleId;
        this.fleetId = fleetId;
        this.tenantId = tenantId;
        this.occurredAt = occurredAt;
    }

    public UUID getVehicleId() { return vehicleId; }
    public UUID getFleetId() { return fleetId; }
    public String getTenantId() { return tenantId; }
    @Override
    public Instant occurredAt() { return occurredAt; }
}

