package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.application.fleet.dto.VehicleResult;
import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.domain.fleet.VehicleRepository;
import com.tuplataforma.core.domain.fleet.ports.input.GetVehicleUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetVehicleService implements GetVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public GetVehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public VehicleResult execute(UUID id) {
        Vehicle v = vehicleRepository.findById(id).orElseThrow(() -> new RuntimeException("Vehicle not found"));
        return new VehicleResult(v.getId(), v.getLicensePlate(), v.getModel(), v.isActive());
    }
}

