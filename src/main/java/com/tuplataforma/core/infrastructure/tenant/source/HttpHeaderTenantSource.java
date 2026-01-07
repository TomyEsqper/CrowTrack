package com.tuplataforma.core.infrastructure.tenant.source;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Optional;

@Component
public class HttpHeaderTenantSource implements TenantSource {

    public static final String TENANT_HEADER = "X-Tenant-Id";

    @Override
    public Optional<String> resolveTenantId(HttpServletRequest request) {
        String tenantId = request.getHeader(TENANT_HEADER);
        if (StringUtils.hasText(tenantId)) {
            return Optional.of(tenantId);
        }
        return Optional.empty();
    }
}
