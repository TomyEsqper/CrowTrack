package com.tuplataforma.core.infrastructure.persistence.company;

import com.tuplataforma.core.domain.company.Company;
import com.tuplataforma.core.domain.company.CompanyConfig;
import com.tuplataforma.core.domain.company.CompanyId;
import com.tuplataforma.core.domain.company.ports.output.CompanyRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class CompanyRepositoryAdapter implements CompanyRepository {

    private final JpaCompanyRepository jpaRepository;

    public CompanyRepositoryAdapter(JpaCompanyRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Company save(Company company) {
        CompanyEntity entity = new CompanyEntity(
            company.getId().getValue(),
            company.getName(),
            company.isActive(),
            company.getConfig() != null ? company.getConfig().getTimezone() : null,
            company.getConfig() != null ? company.getConfig().getLanguage() : null
        );
        
        CompanyEntity saved = jpaRepository.save(entity);
        return mapToDomain(saved);
    }

    @Override
    public Optional<Company> findById(CompanyId id) {
        TenantId tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
             throw new IllegalStateException("Tenant context is missing for query");
        }
        return jpaRepository.findByIdAndTenantId(id.getValue(), tenantId.getValue())
            .map(this::mapToDomain);
    }

    private Company mapToDomain(CompanyEntity saved) {
        return Company.restore(
            new CompanyId(saved.getId()),
            new TenantId(saved.getTenantId()),
            saved.getName(),
            saved.isActive(),
            new CompanyConfig(saved.getTimezone(), saved.getLanguage(), null)
        );
    }
}
