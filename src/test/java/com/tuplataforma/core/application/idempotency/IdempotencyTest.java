package com.tuplataforma.core.application.idempotency;

import com.tuplataforma.core.application.fleet.AssignVehicleToFleetService;
import com.tuplataforma.core.application.fleet.CreateVehicleService;
import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.infrastructure.persistence.fleet.FleetEntity;
import com.tuplataforma.core.infrastructure.persistence.fleet.JpaFleetRepository;
import com.tuplataforma.core.infrastructure.idempotency.JpaIdempotencyRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class IdempotencyTest {

    @Autowired
    private CreateVehicleService createVehicleService;
    @Autowired
    private AssignVehicleToFleetService assignService;
    @Autowired
    private JpaFleetRepository jpaFleetRepository;
    @Autowired
    private JpaIdempotencyRepository idemRepo;

    @Test
    void idempotencia_evitaDobleEjecucion() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u", "p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
        TenantContext.setTenantId(new TenantId("tenant-i"));
        IdempotencyContext.set("KEY-1");
        var v1 = createVehicleService.execute(new CreateVehicleCommand("IDEM-1", "X"));
        var v2 = createVehicleService.execute(new CreateVehicleCommand("IDEM-1", "X"));
        assertEquals(v1.id(), v2.id());
        FleetEntity fleet = new FleetEntity();
        fleet.setName("I");
        fleet.setActive(true);
        FleetEntity f = jpaFleetRepository.save(fleet);
        IdempotencyContext.set("KEY-2");
        var a1 = assignService.execute(new AssignVehicleToFleetCommand(v1.id(), f.getId()));
        var a2 = assignService.execute(new AssignVehicleToFleetCommand(v1.id(), f.getId()));
        assertEquals(a1.fleetId(), a2.fleetId());
        SecurityContextHolder.clearContext();
        TenantContext.clear();
        IdempotencyContext.clear();
        assertTrue(idemRepo.count() >= 2);
    }
}

