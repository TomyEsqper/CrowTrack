package com.tuplataforma.core.infrastructure.persistence.shared;

import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

/**
 * Listener to enforce tenant isolation at the entity level.
 * 
 * <p><strong>TECHNICAL DEBT / TEMPORARY DECISION:</strong></p>
 * Using an EntityListener is a simple and effective starting point, but it has limitations:
 * <ul>
 *   <li>It may be bypassed by bulk updates (JPQL/SQL).</li>
 *   <li>It is coupled to the JPA lifecycle.</li>
 *   <li>It is harder to test in isolation.</li>
 * </ul>
 * 
 * <p><strong>FUTURE MIGRATION PATH:</strong></p>
 * In later phases (scaling/hardening), consider migrating to:
 * <ul>
 *   <li>Hibernate Filters (for transparent read filtering).</li>
 *   <li>MultiTenantConnectionProvider (schema-based isolation).</li>
 *   <li>Mandatory Specifications.</li>
 * </ul>
 */
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
