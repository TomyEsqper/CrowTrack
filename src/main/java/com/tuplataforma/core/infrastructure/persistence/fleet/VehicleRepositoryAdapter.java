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
    private final JpaFleetRepository jpaFleetRepository;

    public VehicleRepositoryAdapter(JpaVehicleRepository jpaRepository, JpaFleetRepository jpaFleetRepository) {
        this.jpaRepository = jpaRepository;
        this.jpaFleetRepository = jpaFleetRepository;
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
        VehicleEntity entity;
        if (domain.getId() != null) {
            entity = jpaRepository.findById(domain.getId()).orElseGet(VehicleEntity::new);
        } else {
            entity = new VehicleEntity();
        }
        entity.setLicensePlate(domain.getLicensePlate());
        entity.setModel(domain.getModel());
        entity.setActive(domain.isActive());
        if (domain.getAssignedFleetId() != null) {
            jpaFleetRepository.findById(domain.getAssignedFleetId()).ifPresent(entity::setFleet);
        } else {
            entity.setFleet(null);
        }
        return entity;
    }

    private Vehicle toDomain(VehicleEntity entity) {
        UUID fleetId = entity.getFleet() != null ? entity.getFleet().getId() : null;
        return new Vehicle(
                entity.getId(),
                new TenantId(entity.getTenantId()),
                entity.getLicensePlate(),
                entity.getModel(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                fleetId
        );
    }
}
