package com.tuplataforma.core.infrastructure.persistence.fleet;

import com.tuplataforma.core.domain.fleet.Fleet;
import com.tuplataforma.core.domain.fleet.FleetRepository;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class FleetRepositoryAdapter implements FleetRepository {

    private final JpaFleetRepository jpaFleetRepository;

    public FleetRepositoryAdapter(JpaFleetRepository jpaFleetRepository) {
        this.jpaFleetRepository = jpaFleetRepository;
    }

    @Override
    public Optional<Fleet> findById(UUID id) {
        return jpaFleetRepository.findById(id).map(this::toDomain);
    }

    private Fleet toDomain(FleetEntity entity) {
        return new Fleet(entity.getId(), new TenantId(entity.getTenantId()), entity.getName(), entity.isActive());
    }
}

