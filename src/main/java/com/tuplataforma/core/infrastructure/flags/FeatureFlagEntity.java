package com.tuplataforma.core.infrastructure.flags;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "feature_flags")
public class FeatureFlagEntity extends BaseTenantEntity {
    @Column(name = "flag_key", nullable = false)
    private String flagKey;
    @Column(name = "enabled", nullable = false)
    private boolean enabled;
    @Column(name = "plan_code")
    private String planCode;
    @Column(name = "environment")
    private String environment;
    @Column(name = "flag_version", nullable = false)
    private String flagVersion;
    public String getFlagKey() { return flagKey; }
    public boolean isEnabled() { return enabled; }
    public String getPlanCode() { return planCode; }
    public String getEnvironment() { return environment; }
    public String getFlagVersion() { return flagVersion; }
    public void setFlagKey(String flagKey) { this.flagKey = flagKey; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }
    public void setEnvironment(String environment) { this.environment = environment; }
    public void setFlagVersion(String flagVersion) { this.flagVersion = flagVersion; }
}
