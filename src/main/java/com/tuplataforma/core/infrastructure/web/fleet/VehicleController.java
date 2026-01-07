package com.tuplataforma.core.infrastructure.web.fleet;

import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.application.fleet.dto.VehicleResult;
import com.tuplataforma.core.domain.fleet.ports.input.CreateVehicleUseCase;
import com.tuplataforma.core.domain.fleet.ports.input.GetVehicleUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/fleet/vehicles")
public class VehicleController {

    private final CreateVehicleUseCase createVehicleUseCase;
    private final GetVehicleUseCase getVehicleUseCase;

    public VehicleController(CreateVehicleUseCase createVehicleUseCase, GetVehicleUseCase getVehicleUseCase) {
        this.createVehicleUseCase = createVehicleUseCase;
        this.getVehicleUseCase = getVehicleUseCase;
    }

    @PostMapping
    public ResponseEntity<VehicleResult> create(@RequestBody CreateVehicleCommand command) {
        VehicleResult result = createVehicleUseCase.execute(command);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleResult> get(@PathVariable UUID id) {
        VehicleResult result = getVehicleUseCase.execute(id);
        return ResponseEntity.ok(result);
    }
}

