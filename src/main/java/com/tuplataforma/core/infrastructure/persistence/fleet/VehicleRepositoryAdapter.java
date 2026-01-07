package com.tuplataforma.core.infrastructure.persistence.fleet;

import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.domain.fleet.VehicleRepository;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class VehicleRepositoryAdapter implements VehicleRepository {

    private final JpaVehicleRepository jpaRepository;

    public VehicleRepositoryAdapter(JpaVehicleRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Vehicle save(Vehicle vehicle) {
        VehicleEntity entity = toEntity(vehicle);
        VehicleEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Vehicle> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Vehicle> findByLicensePlate(String licensePlate) {
        return jpaRepository.findByLicensePlate(licensePlate).map(this::toDomain);
    }

    private VehicleEntity toEntity(Vehicle domain) {
        VehicleEntity entity = new VehicleEntity();
        if (domain.getId() != null) {
            // How to set ID on BaseTenantEntity if it's protected?
            // Reflection or protected setter. But VehicleEntity extends it, so it can access.
            // Wait, BaseTenantEntity fields are protected.
            // But VehicleEntity inherits them.
            // I need a way to set ID.
            // I should add a constructor or setter in BaseTenantEntity or VehicleEntity.
            // VehicleEntity has @Setter (Lombok), so setId should work.
        }
        // Actually, for updates, we should fetch and update.
        // For new, we let BaseTenantEntity generate ID if null.
        // But if domain has ID (update), we need to set it.
        // If domain has no ID, it's new.
        
        // Simpler: Just map fields.
        // If domain.getId() is present, we might need to reference it.
        // But BaseTenantEntity generates ID on PrePersist if null.
        
        // If it's an update, we should ideally load the entity first.
        // But for save(), merge behavior applies.
        
        // Let's use a constructor or setters.
        entity.setLicensePlate(domain.getLicensePlate());
        entity.setModel(domain.getModel());
        entity.setActive(domain.isActive());
        // tenantId is handled by Listener, but if we have it in domain, we might want to check/set?
        // Listener uses Context.
        
        return entity;
    }

    private Vehicle toDomain(VehicleEntity entity) {
        return new Vehicle(
            entity.getId(),
            new TenantId(entity.getTenantId()),
            entity.getLicensePlate(),
            entity.getModel(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
