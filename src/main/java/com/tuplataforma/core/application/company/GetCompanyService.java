package com.tuplataforma.core.application.company;

import com.tuplataforma.core.application.company.dto.CompanyResponse;
import com.tuplataforma.core.domain.company.Company;
import com.tuplataforma.core.domain.company.CompanyId;
import com.tuplataforma.core.domain.company.ports.input.GetCompanyUseCase;
import com.tuplataforma.core.domain.company.ports.output.CompanyRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class GetCompanyService implements GetCompanyUseCase {

    private final CompanyRepository companyRepository;

    public GetCompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CompanyResponse> getCompany(UUID id) {
        Optional<Company> companyOpt = companyRepository.findById(new CompanyId(id));
        
        return companyOpt.map(company -> new CompanyResponse(
            company.getId().getValue(),
            company.getTenantId().getValue(),
            company.getName(),
            company.isActive(),
            company.getConfig().getTimezone(),
            company.getConfig().getLanguage()
        ));
    }
}
