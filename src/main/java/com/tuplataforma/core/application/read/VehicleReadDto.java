package com.tuplataforma.core.application.read;

import java.util.UUID;

public record VehicleReadDto(UUID id, String licensePlate, String model, boolean active, UUID fleetId) {}

