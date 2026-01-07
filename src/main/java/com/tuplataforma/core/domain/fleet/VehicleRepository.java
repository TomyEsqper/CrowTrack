package com.tuplataforma.core.domain.fleet;

import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository {
    Vehicle save(Vehicle vehicle);
    Optional<Vehicle> findById(UUID id);
    Optional<Vehicle> findByLicensePlate(String licensePlate);
}
