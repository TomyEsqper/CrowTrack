package com.tuplataforma.core.application.flags;

import com.tuplataforma.core.infrastructure.flags.JpaFeatureFlagRepository;
import com.tuplataforma.core.infrastructure.flags.FeatureFlagEntity;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class FeatureFlagEvaluator {
    private final JpaFeatureFlagRepository repo;
    private final Environment env;
    public FeatureFlagEvaluator(JpaFeatureFlagRepository repo, Environment env) { this.repo = repo; this.env = env; }
    public boolean isEnabled(String key) {
        String tenant = TenantContext.getTenantId() != null ? TenantContext.getTenantId().getValue() : "none";
        String e = env.getProperty("spring.profiles.active", "dev");
        return repo.findTop1ByTenantIdAndFlagKeyAndEnvironmentOrderByCreatedAtDesc(tenant, key, e).map(FeatureFlagEntity::isEnabled).orElse(true);
    }
}
