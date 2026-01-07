package com.tuplataforma.core.infrastructure.persistence;

import com.tuplataforma.core.infrastructure.persistence.shared.TenantAwareRepositoryImpl;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(
    basePackages = "com.tuplataforma.core",
    repositoryBaseClass = TenantAwareRepositoryImpl.class
)
public class JpaConfig {
}
