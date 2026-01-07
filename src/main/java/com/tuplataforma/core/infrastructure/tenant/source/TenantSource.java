package com.tuplataforma.core.infrastructure.tenant.source;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface TenantSource {
    Optional<String> resolveTenantId(HttpServletRequest request);
}
