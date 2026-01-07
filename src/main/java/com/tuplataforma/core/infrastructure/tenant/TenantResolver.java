package com.tuplataforma.core.infrastructure.tenant;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TenantResolver {

    public static final String TENANT_HEADER = "X-Tenant-Id";

    public String resolveTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (StringUtils.hasText(tenantId)) {
            return tenantId;
        }
        
        // TODO: Implement JWT extraction here when Security is ready
        // String authHeader = request.getHeader("Authorization");
        // ...
        
        return null;
    }
}
