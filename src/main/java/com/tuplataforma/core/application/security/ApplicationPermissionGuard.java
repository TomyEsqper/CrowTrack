package com.tuplataforma.core.application.security;

import org.springframework.stereotype.Component;

@Component
public class ApplicationPermissionGuard {
    private final PermissionResolver resolver;

    public ApplicationPermissionGuard(PermissionResolver resolver) {
        this.resolver = resolver;
    }

    public void require(Permission permission) {
        if (!resolver.resolvePermissions().contains(permission)) throw new PermissionDeniedException();
    }
}

