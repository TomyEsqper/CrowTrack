package com.tuplataforma.core.infrastructure.persistence.shared;

import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import jakarta.persistence.EntityManager;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TenantFilterAspect {

    private final EntityManager entityManager;

    public TenantFilterAspect(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    // Intercept any method in repositories that extend JpaRepository or similar in our domain/infra
    @Before("execution(* com.tuplataforma.core.infrastructure.persistence..*Repository*.*(..))")
    public void enableTenantFilter() {
        TenantId tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            Session session = entityManager.unwrap(Session.class);
            session.enableFilter("tenantFilter").setParameter("tenantId", tenantId.getValue());
        }
    }
}
