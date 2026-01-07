package com.tuplataforma.core.domain.company.ports.output;

import com.tuplataforma.core.domain.company.Company;
import com.tuplataforma.core.domain.company.CompanyId;
import java.util.Optional;

public interface CompanyRepository {
    Company save(Company company);
    Optional<Company> findById(CompanyId id);
}
