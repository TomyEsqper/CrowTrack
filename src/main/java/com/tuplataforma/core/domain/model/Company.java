package com.tuplataforma.core.domain.model;

import java.util.UUID;

public class Company {
    private UUID id;
    private String name;
    private String slug;
    private boolean active;
    private CompanyConfig config;

    public Company(UUID id, String name, String slug, boolean active, CompanyConfig config) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.active = active;
        this.config = config;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public boolean isActive() {
        return active;
    }

    public CompanyConfig getConfig() {
        return config;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    
    public void setConfig(CompanyConfig config) {
        this.config = config;
    }
}
