package com.tuplataforma.core.domain.company;

import com.tuplataforma.core.shared.tenant.TenantId;

public class Company {
    private final CompanyId id;
    private final TenantId tenantId;
    private String name;
    private boolean active;
    private CompanyConfig config;

    // Protected constructor for factory or persistence
    protected Company(CompanyId id, TenantId tenantId, String name, boolean active, CompanyConfig config) {
        if (id == null) throw new IllegalArgumentException("CompanyId is required");
        if (tenantId == null) throw new IllegalArgumentException("TenantId is required");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Name is required");
        
        this.id = id;
        this.tenantId = tenantId;
        this.name = name;
        this.active = active;
        this.config = config;
    }

    // Factory method
    public static Company create(TenantId tenantId, String name, CompanyConfig config) {
        return new Company(
            CompanyId.random(),
            tenantId,
            name,
            true, // Default active
            config
        );
    }
    
    // Reconstruct from persistence
    public static Company restore(CompanyId id, TenantId tenantId, String name, boolean active, CompanyConfig config) {
        return new Company(id, tenantId, name, active, config);
    }

    public CompanyId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public CompanyConfig getConfig() {
        return config;
    }
}
