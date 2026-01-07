package com.tuplataforma.core.domain.company.ports.input;

import com.tuplataforma.core.application.company.dto.CreateCompanyCommand;
import com.tuplataforma.core.application.company.dto.CompanyResponse;

public interface CreateCompanyUseCase {
    CompanyResponse createCompany(CreateCompanyCommand command);
}
