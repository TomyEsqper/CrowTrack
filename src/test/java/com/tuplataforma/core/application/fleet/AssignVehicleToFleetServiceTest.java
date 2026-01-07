package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.AssignmentResult;
import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.domain.fleet.exceptions.VehicleAlreadyAssignedException;
import com.tuplataforma.core.infrastructure.persistence.fleet.FleetEntity;
import com.tuplataforma.core.infrastructure.persistence.fleet.JpaFleetRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class AssignVehicleToFleetServiceTest {

    @Autowired
    private AssignVehicleToFleetService assignService;

    @Autowired
    private CreateVehicleService createVehicleService;

    @Autowired
    private JpaFleetRepository jpaFleetRepository;

    @Test
    void casoFeliz_asignaVehiculoAFleetActiva() {
        TenantContext.setTenantId(new TenantId("tenant-a"));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
        FleetEntity fleet = new FleetEntity();
        fleet.setName("Main Fleet");
        fleet.setActive(true);
        FleetEntity savedFleet = jpaFleetRepository.save(fleet);

        var vehicle = createVehicleService.execute(new CreateVehicleCommand("AAA-111", "Model A"));
        AssignmentResult result = assignService.execute(new AssignVehicleToFleetCommand(vehicle.id(), savedFleet.getId()));
        assertEquals(vehicle.id(), result.vehicleId());
        assertEquals(savedFleet.getId(), result.fleetId());
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void violacion_invariante_vehiculoYaAsignado() {
        TenantContext.setTenantId(new TenantId("tenant-a"));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
        FleetEntity fleet1 = new FleetEntity();
        fleet1.setName("Fleet 1");
        fleet1.setActive(true);
        FleetEntity savedFleet1 = jpaFleetRepository.save(fleet1);

        FleetEntity fleet2 = new FleetEntity();
        fleet2.setName("Fleet 2");
        fleet2.setActive(true);
        FleetEntity savedFleet2 = jpaFleetRepository.save(fleet2);

        var vehicle = createVehicleService.execute(new CreateVehicleCommand("BBB-222", "Model B"));
        assignService.execute(new AssignVehicleToFleetCommand(vehicle.id(), savedFleet1.getId()));
        assertThrows(VehicleAlreadyAssignedException.class, () ->
                assignService.execute(new AssignVehicleToFleetCommand(vehicle.id(), savedFleet2.getId()))
        );
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void crossTenant_fallaAunqueIdsExistan() {
        TenantContext.setTenantId(new TenantId("tenant-b"));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
        FleetEntity fleetB = new FleetEntity();
        fleetB.setName("Fleet B");
        fleetB.setActive(true);
        FleetEntity savedFleetB = jpaFleetRepository.save(fleetB);
        TenantContext.clear();

        TenantContext.setTenantId(new TenantId("tenant-a"));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
        var vehicleA = createVehicleService.execute(new CreateVehicleCommand("CCC-333", "Model C"));
        assertThrows(RuntimeException.class, () ->
                assignService.execute(new AssignVehicleToFleetCommand(vehicleA.id(), savedFleetB.getId()))
        );
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }
}

