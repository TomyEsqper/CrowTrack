package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.AssignmentResult;
import com.tuplataforma.core.domain.fleet.Fleet;
import com.tuplataforma.core.domain.fleet.FleetRepository;
import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.domain.fleet.VehicleRepository;
import com.tuplataforma.core.domain.fleet.ports.input.AssignVehicleToFleetUseCase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssignVehicleToFleetService implements AssignVehicleToFleetUseCase {

    private final VehicleRepository vehicleRepository;
    private final FleetRepository fleetRepository;

    public AssignVehicleToFleetService(VehicleRepository vehicleRepository, FleetRepository fleetRepository) {
        this.vehicleRepository = vehicleRepository;
        this.fleetRepository = fleetRepository;
    }

    @Transactional
    @Override
    public AssignmentResult execute(AssignVehicleToFleetCommand command) {
        Vehicle vehicle = vehicleRepository.findById(command.vehicleId())
                .orElseThrow(() -> new com.tuplataforma.core.domain.fleet.exceptions.VehicleNotFoundException(command.vehicleId()));
        Fleet fleet = fleetRepository.findById(command.fleetId())
                .orElseThrow(() -> new com.tuplataforma.core.domain.fleet.exceptions.FleetNotFoundException(command.fleetId()));
        vehicle.assignTo(fleet);
        Vehicle saved = vehicleRepository.save(vehicle);
        return new AssignmentResult(saved.getId(), saved.getAssignedFleetId());
    }
}

