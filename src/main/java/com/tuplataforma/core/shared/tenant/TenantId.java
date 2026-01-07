package com.tuplataforma.core.shared.tenant;

import java.util.Objects;

public final class TenantId {
    private final String value;

    public TenantId(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("TenantId cannot be null or empty");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantId tenantId = (TenantId) o;
        return Objects.equals(value, tenantId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
