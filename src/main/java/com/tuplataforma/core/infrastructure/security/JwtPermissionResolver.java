package com.tuplataforma.core.infrastructure.security;

import com.tuplataforma.core.application.security.Permission;
import com.tuplataforma.core.application.security.PermissionResolver;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Profile("!test")
public class JwtPermissionResolver implements PermissionResolver {
    @Override
    public Set<Permission> resolvePermissions() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Set<Permission> perms = new HashSet<>();
        if (auth != null && auth.getAuthorities() != null) {
            auth.getAuthorities().forEach(a -> {
                String name = a.getAuthority();
                try {
                    perms.add(Permission.valueOf(name));
                } catch (IllegalArgumentException ignored) {}
            });
        }
        return perms;
    }
}

