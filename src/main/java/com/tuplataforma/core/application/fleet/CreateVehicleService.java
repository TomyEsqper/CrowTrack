package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.application.fleet.dto.VehicleResult;
import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.domain.fleet.VehicleRepository;
import com.tuplataforma.core.domain.fleet.ports.input.CreateVehicleUseCase;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateVehicleService implements CreateVehicleUseCase {

    private final VehicleRepository vehicleRepository;

    public CreateVehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    @Transactional
    @Override
    public VehicleResult execute(CreateVehicleCommand command) {
        Vehicle vehicle = Vehicle.create(
                TenantContext.getTenantId(),
                command.licensePlate(),
                command.model()
        );
        Vehicle saved = vehicleRepository.save(vehicle);
        return new VehicleResult(saved.getId(), saved.getLicensePlate(), saved.getModel(), saved.isActive());
    }
}

