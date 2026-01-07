package com.tuplataforma.core.infrastructure.web.fleet;

import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.AssignmentResult;
import com.tuplataforma.core.domain.fleet.ports.input.AssignVehicleToFleetUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/fleet/vehicles")
public class VehicleAssignmentController {

    private final AssignVehicleToFleetUseCase assignVehicleToFleetUseCase;

    public VehicleAssignmentController(AssignVehicleToFleetUseCase assignVehicleToFleetUseCase) {
        this.assignVehicleToFleetUseCase = assignVehicleToFleetUseCase;
    }

    @PostMapping("/{vehicleId}/assign")
    public ResponseEntity<AssignmentResult> assign(@PathVariable UUID vehicleId, @RequestBody FleetIdBody body) {
        AssignmentResult result = assignVehicleToFleetUseCase.execute(new AssignVehicleToFleetCommand(vehicleId, body.fleetId()));
        return ResponseEntity.ok(result);
    }

    public record FleetIdBody(UUID fleetId) {}
}

