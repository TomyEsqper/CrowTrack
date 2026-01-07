package com.tuplataforma.core.application.fleet.dto;

import java.util.UUID;

public record AssignVehicleToFleetCommand(UUID vehicleId, UUID fleetId) {}

