package com.tuplataforma.core.domain.fleet.ports.input;

import com.tuplataforma.core.application.fleet.dto.VehicleResult;

import java.util.UUID;

public interface GetVehicleUseCase {
    VehicleResult execute(UUID id);
}

