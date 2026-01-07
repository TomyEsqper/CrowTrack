package com.tuplataforma.core.domain.fleet.exceptions;

public class FleetInactiveException extends RuntimeException {
    public FleetInactiveException(String message) {
        super(message);
    }
}

