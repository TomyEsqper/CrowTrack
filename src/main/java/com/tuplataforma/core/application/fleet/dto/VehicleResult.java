package com.tuplataforma.core.application.fleet.dto;

import java.util.UUID;

public record VehicleResult(UUID id, String licensePlate, String model, boolean active) {}

