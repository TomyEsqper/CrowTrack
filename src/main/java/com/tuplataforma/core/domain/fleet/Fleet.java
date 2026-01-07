package com.tuplataforma.core.domain.fleet;

import com.tuplataforma.core.shared.tenant.TenantId;

import java.util.UUID;

public class Fleet {
    private final UUID id;
    private final TenantId tenantId;
    private String name;
    private boolean active;

    public Fleet(UUID id, TenantId tenantId, String name, boolean active) {
        if (tenantId == null) throw new IllegalArgumentException("TenantId requerido");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Nombre requerido");
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.active = active;
    }

    public static Fleet create(TenantId tenantId, String name) {
        return new Fleet(null, tenantId, name, true);
    }

    public UUID getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
}

