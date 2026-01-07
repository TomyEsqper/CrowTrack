package com.tuplataforma.core.infrastructure.security;

import com.tuplataforma.core.application.security.Permission;
import com.tuplataforma.core.application.security.PermissionResolver;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.EnumSet;
import java.util.Set;

@Component
@Profile("test")
public class TestPermissionResolver implements PermissionResolver {
    @Override
    public Set<Permission> resolvePermissions() {
        return EnumSet.allOf(Permission.class);
    }
}

