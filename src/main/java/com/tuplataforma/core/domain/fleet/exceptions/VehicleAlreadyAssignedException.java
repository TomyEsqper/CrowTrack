package com.tuplataforma.core.domain.fleet.exceptions;

public class VehicleAlreadyAssignedException extends RuntimeException {
    public VehicleAlreadyAssignedException(String message) {
        super(message);
    }
}

