package com.tuplataforma.core.infrastructure.web.company;

import com.tuplataforma.core.application.company.dto.CompanyResponse;
import com.tuplataforma.core.application.company.dto.CreateCompanyCommand;
import com.tuplataforma.core.domain.company.ports.input.CreateCompanyUseCase;
import com.tuplataforma.core.domain.company.ports.input.GetCompanyUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@RequestMapping("/platform/companies")
public class CompanyController {

    private final CreateCompanyUseCase createCompanyUseCase;
    private final GetCompanyUseCase getCompanyUseCase;

    public CompanyController(CreateCompanyUseCase createCompanyUseCase, GetCompanyUseCase getCompanyUseCase) {
        this.createCompanyUseCase = createCompanyUseCase;
        this.getCompanyUseCase = getCompanyUseCase;
    }

    @PostMapping
    public ResponseEntity<CompanyResponse> createCompany(@RequestBody CreateCompanyCommand command) {
        CompanyResponse response = createCompanyUseCase.createCompany(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyResponse> getCompany(@PathVariable UUID id) {
        return getCompanyUseCase.getCompany(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }
}
