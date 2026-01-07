package com.tuplataforma.core.infrastructure.persistence.fleet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaVehicleRepository extends JpaRepository<VehicleEntity, UUID> {
    Optional<VehicleEntity> findByLicensePlate(String licensePlate);
}
