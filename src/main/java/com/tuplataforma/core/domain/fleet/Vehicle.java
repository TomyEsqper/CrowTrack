package com.tuplataforma.core.domain.fleet;

import com.tuplataforma.core.shared.tenant.TenantId;
import com.tuplataforma.core.domain.fleet.exceptions.InvalidLicensePlateException;

import java.time.Instant;
import java.util.UUID;

public class Vehicle {
    private final UUID id;
    private final TenantId tenantId;
    private String licensePlate;
    private String model;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;

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
}
