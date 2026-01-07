package com.tuplataforma.core.application.security;

import java.util.Set;

public interface PermissionResolver {
    Set<Permission> resolvePermissions();
}

