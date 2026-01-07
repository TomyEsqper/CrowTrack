package com.tuplataforma.core.domain.fleet.exceptions;

import java.util.UUID;

public class FleetNotFoundException extends RuntimeException {
    public FleetNotFoundException(UUID id) {
        super("Fleet not found: " + id);
    }
}

