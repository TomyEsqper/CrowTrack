package com.tuplataforma.core.application.usecase;

import com.tuplataforma.core.application.dto.CreateTenantRequest;
import com.tuplataforma.core.domain.model.Tenant;
import com.tuplataforma.core.domain.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTenantUseCase {

    private final TenantRepository tenantRepository;

    @Transactional
    public Tenant execute(CreateTenantRequest request) {
        log.info("Creating tenant with ID: {}", request.getId());

        if (tenantRepository.existsById(request.getId())) {
            throw new IllegalArgumentException("Tenant with ID " + request.getId() + " already exists");
        }

        Tenant tenant = Tenant.builder()
                .id(request.getId())
                .name(request.getName())
                .status("ACTIVE")
                .createdAt(Instant.now())
                .build();

        return tenantRepository.save(tenant);
    }
}
