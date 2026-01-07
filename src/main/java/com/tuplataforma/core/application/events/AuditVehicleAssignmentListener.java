package com.tuplataforma.core.application.events;

import com.tuplataforma.core.domain.events.VehicleAssignedToFleetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AuditVehicleAssignmentListener {
    private static final Logger log = LoggerFactory.getLogger(AuditVehicleAssignmentListener.class);

    @EventListener
    public void on(VehicleAssignedToFleetEvent event) {
        log.info("Audit: vehicle {} assigned to fleet {} in tenant {} at {}", event.getVehicleId(), event.getFleetId(), event.getTenantId(), event.occurredAt());
    }
}

