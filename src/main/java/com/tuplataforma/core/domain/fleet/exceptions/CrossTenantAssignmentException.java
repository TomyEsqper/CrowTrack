package com.tuplataforma.core.domain.fleet.exceptions;

public class CrossTenantAssignmentException extends RuntimeException {
    public CrossTenantAssignmentException(String message) {
        super(message);
    }
}

