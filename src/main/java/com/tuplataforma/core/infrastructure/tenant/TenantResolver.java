package com.tuplataforma.core.infrastructure.tenant;

import com.tuplataforma.core.infrastructure.tenant.source.TenantSource;
import com.tuplataforma.core.shared.tenant.TenantId;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TenantResolver {

    private final List<TenantSource> tenantSources;

    public TenantResolver(List<TenantSource> tenantSources) {
        this.tenantSources = tenantSources;
    }

    public Optional<TenantId> resolveTenantId(HttpServletRequest request) {
        for (TenantSource source : tenantSources) {
            Optional<String> tenantIdStr = source.resolveTenantId(request);
            if (tenantIdStr.isPresent()) {
                try {
                    return Optional.of(new TenantId(tenantIdStr.get()));
                } catch (IllegalArgumentException e) {
                    // Log invalid tenant format but continue or fail? 
                    // For now, let's treat it as not found from this source
                }
            }
        }
        return Optional.empty();
    }
}
