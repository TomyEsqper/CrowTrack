package com.tuplataforma.core.application.events;

import com.tuplataforma.core.application.fleet.AssignVehicleToFleetService;
import com.tuplataforma.core.application.fleet.CreateVehicleService;
import com.tuplataforma.core.application.fleet.dto.AssignVehicleToFleetCommand;
import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.domain.events.VehicleAssignedToFleetEvent;
import com.tuplataforma.core.infrastructure.persistence.fleet.FleetEntity;
import com.tuplataforma.core.infrastructure.persistence.fleet.JpaFleetRepository;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DomainEventPublishingTest {

    @Autowired
    private AssignVehicleToFleetService assignService;
    @Autowired
    private CreateVehicleService createVehicleService;
    @Autowired
    private JpaFleetRepository jpaFleetRepository;
    @Autowired
    private TestCollector collector;

    @Test
    @Commit
    void publicaEventoDespuesDelCommit() {
        TenantContext.setTenantId(new TenantId("tenant-e"));
        FleetEntity fleet = new FleetEntity();
        fleet.setName("Audit Fleet");
        fleet.setActive(true);
        FleetEntity savedFleet = jpaFleetRepository.save(fleet);
        var vehicle = createVehicleService.execute(new CreateVehicleCommand("EEE-555", "Model E"));
        assignService.execute(new AssignVehicleToFleetCommand(vehicle.id(), savedFleet.getId()));
        // commit happens at transaction end; but publisher registers afterCommit, and our test is transactional.
        // We can manually flush to trigger commit behavior on synchronization, but Spring Test rolls back.
        // The publisher publishes even if no synchronization active; we rely on synchronization active to publish afterCommit.
        // Use TransactionSynchronizationManager ensures afterCommit; but in test with @Transactional, afterCommit triggers before rollback.
        assertTrue(collector.count() >= 1);
        TenantContext.clear();
    }

    @Component
    static class TestCollector {
        private final List<VehicleAssignedToFleetEvent> events = new ArrayList<>();
        @EventListener
        public void on(VehicleAssignedToFleetEvent event) {
            events.add(event);
        }
        int count() { return events.size(); }
        void clear() { events.clear(); }
    }
}

