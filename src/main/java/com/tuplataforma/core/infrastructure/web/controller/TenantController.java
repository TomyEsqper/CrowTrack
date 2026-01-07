package com.tuplataforma.core.infrastructure.web.controller;

import com.tuplataforma.core.application.dto.CreateTenantRequest;
import com.tuplataforma.core.application.usecase.CreateTenantUseCase;
import com.tuplataforma.core.domain.model.Tenant;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/platform/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final CreateTenantUseCase createTenantUseCase;

    @PostMapping
    public ResponseEntity<Tenant> createTenant(@Valid @RequestBody CreateTenantRequest request) {
        Tenant tenant = createTenantUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(tenant);
    }
}
