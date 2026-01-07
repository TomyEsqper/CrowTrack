package com.tuplataforma.core.infrastructure.persistence.fleet;

import com.tuplataforma.core.application.fleet.VehicleService;
import com.tuplataforma.core.domain.fleet.Vehicle;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class VehicleIsolationTest {

    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private JpaVehicleRepository jpaVehicleRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void testTenantIsolation() {
        // 1. Create Vehicle in Tenant A
        TenantContext.setTenantId(new TenantId("tenant-a"));
        Vehicle vehicleA = vehicleService.createVehicle("BUS-001", "Volvo");
        UUID idA = vehicleA.getId();
        assertNotNull(idA);
        
        // Force flush to DB so it exists
        entityManager.flush();
        // Clear L1 cache to force query on next fetch
        entityManager.clear();
        TenantContext.clear();

        // 2. Switch to Tenant B
        TenantContext.setTenantId(new TenantId("tenant-b"));
        
        // 3. Verify Vehicle A is NOT visible via Service
        assertThrows(RuntimeException.class, () -> vehicleService.getVehicle(idA));
        
        // 4. Verify Vehicle A is NOT visible via Repository (Direct)
        assertTrue(jpaVehicleRepository.findById(idA).isEmpty(), "Vehicle A should not be visible to Tenant B");

        // 5. Create Vehicle in Tenant B with SAME plate
        Vehicle vehicleB = vehicleService.createVehicle("BUS-001", "Mercedes");
        UUID idB = vehicleB.getId();
        assertNotNull(idB);
        assertNotEquals(idA, idB);
        
        entityManager.flush();
        entityManager.clear();
        
        // 6. Verify Vehicle B IS visible
        Vehicle fetchedB = vehicleService.getVehicle(idB);
        assertEquals("Mercedes", fetchedB.getModel());
        
        TenantContext.clear();
        entityManager.clear();
        
        // 7. Verify we can see A again if we switch back
        TenantContext.setTenantId(new TenantId("tenant-a"));
        Vehicle fetchedA = vehicleService.getVehicle(idA);
        assertEquals("Volvo", fetchedA.getModel());
        TenantContext.clear();
    }
}
