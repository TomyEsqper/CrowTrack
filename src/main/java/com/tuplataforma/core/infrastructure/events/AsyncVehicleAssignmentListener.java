package com.tuplataforma.core.infrastructure.events;

import com.tuplataforma.core.domain.events.VehicleAssignedToFleetEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AsyncVehicleAssignmentListener {
    private static final Logger log = LoggerFactory.getLogger(AsyncVehicleAssignmentListener.class);

    @EventListener
    public void on(VehicleAssignedToFleetEvent event) {
        // Prepared for async messaging (Kafka/Rabbit) - decoupled structure
        log.info("Async-prep: dispatching VehicleAssignedToFleetEvent payload to message bus (placeholder). vehicle={}, fleet={}, tenant={}",
                event.getVehicleId(), event.getFleetId(), event.getTenantId());
    }
}

