package com.tuplataforma.core.infrastructure.persistence.shared;

public interface TenantAware {
    void setTenantId(String tenantId);
    String getTenantId();
}
