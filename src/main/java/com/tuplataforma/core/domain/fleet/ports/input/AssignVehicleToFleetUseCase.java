package com.tuplataforma.core.domain.fleet.ports.input;

import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.AssignmentResult;

public interface AssignVehicleToFleetUseCase {
    AssignmentResult execute(AssignVehicleToFleetCommand command);
}

