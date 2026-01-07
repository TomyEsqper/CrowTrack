package com.tuplataforma.core.domain.fleet;

import com.tuplataforma.core.shared.tenant.TenantId;
import com.tuplataforma.core.domain.fleet.exceptions.InvalidLicensePlateException;
import com.tuplataforma.core.domain.events.DomainEvent;
import com.tuplataforma.core.domain.events.VehicleAssignedToFleetEvent;
import com.tuplataforma.core.shared.correlation.CorrelationContext;

import java.time.Instant;
import java.util.UUID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Vehicle {
    private final UUID id;
    private final TenantId tenantId;
    private String licensePlate;
    private String model;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private UUID assignedFleetId;
    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public Vehicle(UUID id, TenantId tenantId, String licensePlate, String model, boolean active, Instant createdAt, Instant updatedAt) {
        if (tenantId == null) throw new IllegalArgumentException("TenantId requerido");
        if (licensePlate == null || licensePlate.isBlank()) throw new InvalidLicensePlateException("Matrícula requerida");
        this.id = id;
        this.tenantId = tenantId;
        this.licensePlate = licensePlate;
        this.model = model;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Vehicle(UUID id, TenantId tenantId, String licensePlate, String model, boolean active, Instant createdAt, Instant updatedAt, UUID assignedFleetId) {
        this(id, tenantId, licensePlate, model, active, createdAt, updatedAt);
        this.assignedFleetId = assignedFleetId;
    }

    public static Vehicle create(TenantId tenantId, String licensePlate, String model) {
        return new Vehicle(null, tenantId, licensePlate, model, true, null, null);
    }

    public UUID getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getLicensePlate() { return licensePlate; }
    public String getModel() { return model; }
    public boolean isActive() { return active; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public UUID getAssignedFleetId() { return assignedFleetId; }

    public void assignTo(Fleet fleet) {
        if (!fleet.isActive()) throw new com.tuplataforma.core.domain.fleet.exceptions.FleetInactiveException("Fleet inactiva");
        if (!fleet.getTenantId().equals(this.tenantId)) throw new com.tuplataforma.core.domain.fleet.exceptions.CrossTenantAssignmentException("Cross-tenant no permitido");
        if (this.assignedFleetId != null && !this.assignedFleetId.equals(fleet.getId())) throw new com.tuplataforma.core.domain.fleet.exceptions.VehicleAlreadyAssignedException("Vehículo ya asignado");
        this.assignedFleetId = fleet.getId();
        domainEvents.add(new VehicleAssignedToFleetEvent(this.id, this.assignedFleetId, this.tenantId.getValue(), Instant.now(), CorrelationContext.get()));
    }

    public List<DomainEvent> collectDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    public void clearDomainEvents() {
        domainEvents.clear();
    }
}
