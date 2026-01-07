package com.tuplataforma.core.shared.tenant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TenantContext {
    private static final Logger log = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<TenantId> CURRENT_TENANT = new ThreadLocal<>();

    public static void setTenantId(TenantId tenantId) {
        log.debug("Setting tenantId: {}", tenantId);
        CURRENT_TENANT.set(tenantId);
    }

    public static TenantId getTenantId() {
        return CURRENT_TENANT.get();
    }

    public static void clear() {
        log.debug("Clearing tenantId");
        CURRENT_TENANT.remove();
    }
}
