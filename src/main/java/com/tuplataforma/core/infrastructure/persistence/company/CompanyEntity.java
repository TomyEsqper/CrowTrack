package com.tuplataforma.core.infrastructure.persistence.company;

import com.tuplataforma.core.infrastructure.persistence.shared.TenantAwareEntity;
import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "companies", indexes = {
    @Index(name = "idx_company_tenant", columnList = "tenant_id")
})
public class CompanyEntity extends TenantAwareEntity {
    
    @Id
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;
    
    @Column(name = "timezone")
    private String timezone;
    
    @Column(name = "language")
    private String language;

    public CompanyEntity() {}

    public CompanyEntity(UUID id, String name, boolean active, String timezone, String language) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.timezone = timezone;
        this.language = language;
    }
    
    // Getters
    public UUID getId() { return id; }
    public String getName() { return name; }
    public boolean isActive() { return active; }
    public String getTimezone() { return timezone; }
    public String getLanguage() { return language; }
}
