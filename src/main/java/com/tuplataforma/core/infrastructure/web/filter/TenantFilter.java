package com.tuplataforma.core.infrastructure.web.filter;

import com.tuplataforma.core.shared.tenant.TenantContext;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-Tenant-Id";
    private static final String PLATFORM_PATH_PREFIX = "/api/v1/platform";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        try {
            String tenantId = req.getHeader(TENANT_HEADER);
            String path = req.getRequestURI();

            if (tenantId != null && !tenantId.isBlank()) {
                TenantContext.setTenantId(tenantId);
            } else {
                // Enforce tenant for non-platform paths
                // Skipping check for actuator, error, and platform paths
                if (!path.startsWith(PLATFORM_PATH_PREFIX) && 
                    !path.startsWith("/actuator") && 
                    !path.startsWith("/error")) {
                    
                    res.sendError(HttpServletResponse.SC_BAD_REQUEST, "Missing Tenant ID in header X-Tenant-Id");
                    return;
                }
            }

            chain.doFilter(request, response);

        } finally {
            TenantContext.clear();
        }
    }
}
