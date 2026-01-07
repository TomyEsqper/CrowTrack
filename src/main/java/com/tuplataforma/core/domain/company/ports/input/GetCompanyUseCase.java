package com.tuplataforma.core.domain.company.ports.input;

import com.tuplataforma.core.application.company.dto.CompanyResponse;
import java.util.UUID;
import java.util.Optional;

public interface GetCompanyUseCase {
    Optional<CompanyResponse> getCompany(UUID id);
}
