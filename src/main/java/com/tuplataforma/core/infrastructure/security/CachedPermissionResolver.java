package com.tuplataforma.core.infrastructure.security;

import com.tuplataforma.core.application.security.Permission;
import com.tuplataforma.core.application.security.PermissionResolver;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Profile("!test")
public class CachedPermissionResolver implements PermissionResolver {
    private final JwtPermissionResolver delegate;
    private final Map<String, Set<Permission>> cache = new ConcurrentHashMap<>();
    public CachedPermissionResolver(JwtPermissionResolver delegate) { this.delegate = delegate; }
    @Override
    public Set<Permission> resolvePermissions() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String tenant = TenantContext.getTenantId() != null ? TenantContext.getTenantId().getValue() : "none";
        String user = auth != null ? auth.getName() : "anon";
        String key = tenant + "|v1|" + user;
        return cache.computeIfAbsent(key, k -> delegate.resolvePermissions());
    }
    public void invalidateTenant(String tenantId) {
        cache.keySet().removeIf(k -> k.startsWith(tenantId + "|"));
    }
    public void invalidateUser(String tenantId, String userName) {
        cache.remove(tenantId + "|v1|" + userName);
    }
}

