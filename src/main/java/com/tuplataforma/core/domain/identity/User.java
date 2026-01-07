package com.tuplataforma.core.domain.identity;

import com.tuplataforma.core.shared.tenant.TenantId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class User {
    private final UserId id;
    private final TenantId tenantId;
    private final Email email;
    private final PasswordHash passwordHash;
    private final Set<Role> roles;
    private boolean active;

    private User(UserId id, TenantId tenantId, Email email, PasswordHash passwordHash, Set<Role> roles, boolean active) {
        this.id = id;
        this.tenantId = tenantId;
        this.email = email;
        this.passwordHash = passwordHash;
        this.roles = roles != null ? new HashSet<>(roles) : new HashSet<>();
        this.active = active;
    }

    public static User create(TenantId tenantId, Email email, PasswordHash passwordHash, Set<Role> roles) {
        if (tenantId == null) throw new IllegalArgumentException("TenantId is required");
        if (email == null) throw new IllegalArgumentException("Email is required");
        if (passwordHash == null) throw new IllegalArgumentException("PasswordHash is required");
        
        return new User(UserId.random(), tenantId, email, passwordHash, roles, true);
    }

    public static User restore(UserId id, TenantId tenantId, Email email, PasswordHash passwordHash, Set<Role> roles, boolean active) {
        return new User(id, tenantId, email, passwordHash, roles, active);
    }

    public UserId getId() {
        return id;
    }

    public TenantId getTenantId() {
        return tenantId;
    }

    public Email getEmail() {
        return email;
    }

    public PasswordHash getPasswordHash() {
        return passwordHash;
    }

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
    }

    public boolean isActive() {
        return active;
    }
}
