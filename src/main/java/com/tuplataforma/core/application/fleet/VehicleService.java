package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.domain.fleet.VehicleRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VehicleService {
    
    private final VehicleRepository vehicleRepository;

    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    public Vehicle createVehicle(String licensePlate, String model) {
        Vehicle vehicle = Vehicle.create(TenantContext.getTenantId(), licensePlate, model);
        return vehicleRepository.save(vehicle);
    }

    @Transactional(readOnly = true)
    public Vehicle getVehicle(UUID id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));
    }
}
