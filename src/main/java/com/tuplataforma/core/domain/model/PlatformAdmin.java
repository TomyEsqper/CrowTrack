package com.tuplataforma.core.domain.model;

import java.util.UUID;

public class PlatformAdmin {
    private UUID id;
    private String email;
    private String name;
    private boolean active;

    public PlatformAdmin(UUID id, String email, String name, boolean active) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
