package com.tuplataforma.core.infrastructure.persistence.shared;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(TenantEntityListener.class)
public abstract class TenantAwareEntity implements TenantAware {

    @Column(name = "tenant_id", nullable = false, updatable = false)
    protected String tenantId;

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
