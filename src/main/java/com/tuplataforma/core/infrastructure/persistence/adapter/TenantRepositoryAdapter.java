package com.tuplataforma.core.infrastructure.persistence.adapter;

import com.tuplataforma.core.domain.model.Tenant;
import com.tuplataforma.core.domain.repository.TenantRepository;
import com.tuplataforma.core.infrastructure.persistence.entity.TenantEntity;
import com.tuplataforma.core.infrastructure.persistence.repository.JpaTenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TenantRepositoryAdapter implements TenantRepository {

    private final JpaTenantRepository jpaRepository;

    @Override
    public Tenant save(Tenant tenant) {
        TenantEntity entity = toEntity(tenant);
        TenantEntity savedEntity = jpaRepository.save(entity);
        return toDomain(savedEntity);
    }

    @Override
    public Optional<Tenant> findById(String id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(String id) {
        return jpaRepository.existsById(id);
    }

    private TenantEntity toEntity(Tenant tenant) {
        return TenantEntity.builder()
                .id(tenant.getId())
                .name(tenant.getName())
                .status(tenant.getStatus())
                .createdAt(tenant.getCreatedAt())
                .build();
    }

    private Tenant toDomain(TenantEntity entity) {
        return Tenant.builder()
                .id(entity.getId())
                .name(entity.getName())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}
