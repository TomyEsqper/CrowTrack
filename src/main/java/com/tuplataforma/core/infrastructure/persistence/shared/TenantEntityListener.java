package com.tuplataforma.core.infrastructure.persistence.shared;

import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

public class TenantEntityListener {

    @PrePersist
    @PreUpdate
    public void setTenantId(Object entity) {
        if (entity instanceof TenantAware) {
            TenantId tenantId = TenantContext.getTenantId();
            if (tenantId == null) {
                throw new IllegalStateException("Tenant context is missing for persistence operation");
            }
            // Only set if not already set? Or force?
            // "El tenant se obtiene exclusivamente desde TenantContext" -> Force override/set.
            // But if it's updatable=false, PreUpdate might fail or be ignored.
            // Usually tenant doesn't change.
            // I'll set it.
            ((TenantAware) entity).setTenantId(tenantId.getValue());
        }
    }
}
