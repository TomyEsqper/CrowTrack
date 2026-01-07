package com.tuplataforma.core.infrastructure.persistence.fleet;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "fleets", indexes = {
        @Index(name = "idx_fleet_tenant", columnList = "tenant_id")
})
public class FleetEntity extends BaseTenantEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    public String getName() { return name; }
    public boolean isActive() { return active; }
    public void setName(String name) { this.name = name; }
    public void setActive(boolean active) { this.active = active; }
}

