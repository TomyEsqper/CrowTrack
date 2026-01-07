package com.tuplataforma.core.shared.user;

import com.tuplataforma.core.domain.identity.Role;
import com.tuplataforma.core.shared.tenant.TenantId;
import java.util.Collections;
import java.util.Set;

public class UserContext {
    private static final ThreadLocal<UserContextData> CONTEXT = new ThreadLocal<>();

    private record UserContextData(String userId, TenantId tenantId, Set<Role> roles) {}

    public static void set(String userId, TenantId tenantId, Set<Role> roles) {
        CONTEXT.set(new UserContextData(userId, tenantId, roles));
    }

    public static void clear() {
        CONTEXT.remove();
    }

    public static String getUserId() {
        UserContextData data = CONTEXT.get();
        return data != null ? data.userId : null;
    }

    public static TenantId getTenantId() {
        UserContextData data = CONTEXT.get();
        return data != null ? data.tenantId : null;
    }

    public static Set<Role> getRoles() {
        UserContextData data = CONTEXT.get();
        return data != null ? Collections.unmodifiableSet(data.roles) : Collections.emptySet();
    }
}
