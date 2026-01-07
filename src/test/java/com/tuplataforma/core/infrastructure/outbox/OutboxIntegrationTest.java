package com.tuplataforma.core.infrastructure.outbox;

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
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class OutboxIntegrationTest {

    @Autowired
    private AssignVehicleToFleetService assignService;
    @Autowired
    private CreateVehicleService createVehicleService;
    @Autowired
    private JpaFleetRepository jpaFleetRepository;
    @Autowired
    private JpaOutboxEventRepository outboxRepository;
    @Autowired
    private OutboxPublisherService outboxPublisherService;

    @Test
    @Transactional
    void outboxSeLlenaEnTransaccionYSePublicaPosteriormente() {
        TenantContext.setTenantId(new TenantId("tenant-z"));
        FleetEntity fleet = new FleetEntity();
        fleet.setName("Z-Fleet");
        fleet.setActive(true);
        FleetEntity savedFleet = jpaFleetRepository.save(fleet);
        var vehicle = createVehicleService.execute(new CreateVehicleCommand("ZZZ-999", "Model Z"));
        assignService.execute(new AssignVehicleToFleetCommand(vehicle.id(), savedFleet.getId()));

        assertTrue(outboxRepository.findTop100ByPublishedFalseOrderByOccurredOnAsc().size() >= 1);
        int published = outboxPublisherService.publishPending();
        assertTrue(published >= 1);
        TenantContext.clear();
    }

}

