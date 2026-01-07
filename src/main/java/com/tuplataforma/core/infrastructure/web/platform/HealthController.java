package com.tuplataforma.core.infrastructure.web.platform;

import com.tuplataforma.core.infrastructure.persistence.fleet.JpaVehicleRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/platform/health")
public class HealthController {
    private final JpaVehicleRepository vehicleRepo;
    public HealthController(JpaVehicleRepository vehicleRepo) { this.vehicleRepo = vehicleRepo; }
    @GetMapping("/liveness")
    public ResponseEntity<String> liveness() { return ResponseEntity.ok("alive"); }
    @GetMapping("/readiness")
    public ResponseEntity<String> readiness() {
        vehicleRepo.count();
        return ResponseEntity.ok("ready");
    }
    @GetMapping("/tenant")
    public ResponseEntity<String> tenant() {
        return ResponseEntity.ok(TenantContext.getTenantId() != null ? "tenant_set" : "tenant_missing");
    }
}

