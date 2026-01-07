package com.tuplataforma.core.performance;

import com.tuplataforma.core.application.fleet.AssignVehicleToFleetService;
import com.tuplataforma.core.application.fleet.CreateVehicleService;
import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.infrastructure.persistence.fleet.FleetEntity;
import com.tuplataforma.core.infrastructure.persistence.fleet.JpaFleetRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ConcurrencyAssignTest {
    @Autowired
    private CreateVehicleService createVehicleService;
    @Autowired
    private AssignVehicleToFleetService assignService;
    @Autowired
    private JpaFleetRepository jpaFleetRepository;

    @Test
    void concurrenciaEnAssign_conflictosControlados() throws InterruptedException {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
        TenantContext.setTenantId(new TenantId("tenant-c"));
        FleetEntity fleet = new FleetEntity();
        fleet.setName("C");
        fleet.setActive(true);
        FleetEntity f = jpaFleetRepository.save(fleet);
        var v = createVehicleService.execute(new CreateVehicleCommand("CONC-1", "M"));
        int threads = 5;
        ExecutorService es = Executors.newFixedThreadPool(threads);
        List<Future<Boolean>> futures = new ArrayList<>();
        org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
                new TestingAuthenticationToken("u","p",
                        java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
        assignService.execute(new AssignVehicleToFleetCommand(v.id(), f.getId()));
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        for (int i=0;i<threads;i++) {
            futures.add(es.submit(() -> {
                try {
                    org.springframework.security.core.context.SecurityContextHolder.getContext().setAuthentication(
                            new TestingAuthenticationToken("u","p",
                                    java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"), new SimpleGrantedAuthority("VEHICLE_ASSIGN"))));
                    assignService.execute(new AssignVehicleToFleetCommand(v.id(), f.getId()));
                    org.springframework.security.core.context.SecurityContextHolder.clearContext();
                    return true;
                } catch (Exception ex) {
                    org.springframework.security.core.context.SecurityContextHolder.clearContext();
                    return false;
                }
            }));
        }
        es.shutdown();
        es.awaitTermination(5, TimeUnit.SECONDS);
        long success = futures.stream().filter(fut -> {
            try { return fut.get(); } catch (Exception e) { return false; }
        }).count();
        assertEquals(0, success);
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }
}

