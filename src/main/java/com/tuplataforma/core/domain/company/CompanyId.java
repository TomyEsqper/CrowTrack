package com.tuplataforma.core.domain.company;

import java.util.UUID;
import java.util.Objects;

public final class CompanyId {
    private final UUID value;

    public CompanyId(UUID value) {
        if (value == null) {
            throw new IllegalArgumentException("CompanyId cannot be null");
        }
        this.value = value;
    }

    public static CompanyId random() {
        return new CompanyId(UUID.randomUUID());
    }
    
    public static CompanyId fromString(String value) {
        return new CompanyId(UUID.fromString(value));
    }

    public UUID getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CompanyId companyId = (CompanyId) o;
        return Objects.equals(value, companyId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
