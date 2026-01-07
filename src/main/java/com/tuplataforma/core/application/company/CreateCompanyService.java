package com.tuplataforma.core.application.company;

import com.tuplataforma.core.application.company.dto.CompanyResponse;
import com.tuplataforma.core.application.company.dto.CreateCompanyCommand;
import com.tuplataforma.core.domain.company.Company;
import com.tuplataforma.core.domain.company.CompanyConfig;
import com.tuplataforma.core.domain.company.ports.input.CreateCompanyUseCase;
import com.tuplataforma.core.domain.company.ports.output.CompanyRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateCompanyService implements CreateCompanyUseCase {

    private final CompanyRepository companyRepository;

    public CreateCompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional
    public CompanyResponse createCompany(CreateCompanyCommand command) {
        TenantId tenantId = TenantContext.getTenantId();
        if (tenantId == null) {
            // Should be handled by TenantFilter/Aspect, but double check
            throw new IllegalStateException("Tenant context is missing");
        }

        CompanyConfig config = new CompanyConfig(
            command.timezone(),
            command.language(),
            command.extraSettings()
        );

        Company company = Company.create(tenantId, command.name(), config);
        Company saved = companyRepository.save(company);

        return new CompanyResponse(
            saved.getId().getValue(),
            saved.getTenantId().getValue(),
            saved.getName(),
            saved.isActive(),
            saved.getConfig().getTimezone(),
            saved.getConfig().getLanguage()
        );
    }
}
