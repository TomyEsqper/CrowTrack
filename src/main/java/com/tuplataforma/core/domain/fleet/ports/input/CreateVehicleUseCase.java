package com.tuplataforma.core.domain.fleet.ports.input;

import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.application.fleet.dto.VehicleResult;

public interface CreateVehicleUseCase {
    VehicleResult execute(CreateVehicleCommand command);
}

