package com.tuplataforma.core.infrastructure.web.errors;

import com.tuplataforma.core.application.errors.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(com.tuplataforma.core.application.security.PermissionDeniedException.class)
    public ResponseEntity<ApiError> handlePermDenied(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.PERMISSION_DENIED.name(), "Permission denied"), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(com.tuplataforma.core.application.governance.TenantSuspendedException.class)
    public ResponseEntity<ApiError> handleTenantSuspended(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.TENANT_SUSPENDED.name(), "Tenant suspended"), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(com.tuplataforma.core.application.governance.ModuleDisabledException.class)
    public ResponseEntity<ApiError> handleModuleDisabled(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.MODULE_DISABLED.name(), "Module disabled"), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(com.tuplataforma.core.application.governance.QuotaExceededException.class)
    public ResponseEntity<ApiError> handleQuotaExceeded(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.QUOTA_EXCEEDED.name(), "Quota exceeded"), HttpStatus.TOO_MANY_REQUESTS);
    }
    @ExceptionHandler(com.tuplataforma.core.application.subscription.PlanRestrictionException.class)
    public ResponseEntity<ApiError> handlePlanRestriction(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.PLAN_RESTRICTION.name(), "Plan restriction"), HttpStatus.FORBIDDEN);
    }
    @ExceptionHandler(com.tuplataforma.core.domain.fleet.exceptions.VehicleNotFoundException.class)
    public ResponseEntity<ApiError> handleVehicleNotFound(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.VEHICLE_NOT_FOUND.name(), "Vehicle not found"), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(com.tuplataforma.core.domain.fleet.exceptions.FleetNotFoundException.class)
    public ResponseEntity<ApiError> handleFleetNotFound(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.FLEET_NOT_FOUND.name(), "Fleet not found"), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(com.tuplataforma.core.domain.fleet.exceptions.InvalidLicensePlateException.class)
    public ResponseEntity<ApiError> handleInvalidPlate(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.INVALID_LICENSE_PLATE.name(), "Invalid license plate"), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(com.tuplataforma.core.domain.fleet.exceptions.VehicleAlreadyAssignedException.class)
    public ResponseEntity<ApiError> handleAlreadyAssigned(RuntimeException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.VEHICLE_ALREADY_ASSIGNED.name(), "Vehicle already assigned"), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(jakarta.persistence.OptimisticLockException.class)
    public ResponseEntity<ApiError> handleOptimistic(jakarta.persistence.OptimisticLockException ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.CONCURRENT_MODIFICATION.name(), "Concurrent modification"), HttpStatus.CONFLICT);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpected(Exception ex) {
        return new ResponseEntity<>(new ApiError(ErrorCode.UNEXPECTED_ERROR.name(), "Unexpected error"), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

