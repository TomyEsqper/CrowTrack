package com.tuplataforma.core.infrastructure.events;

import com.tuplataforma.core.domain.events.VehicleAssignedToFleetEvent;
import com.tuplataforma.core.infrastructure.persistence.deadletter.DeadLetterEvent;
import com.tuplataforma.core.infrastructure.persistence.deadletter.JpaDeadLetterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AsyncVehicleAssignmentListener {
    private static final Logger log = LoggerFactory.getLogger(AsyncVehicleAssignmentListener.class);
    private final JpaDeadLetterRepository deadLetterRepository;

    private int failureCount = 0;
    private boolean circuitOpen = false;
    private long openUntil = 0;

    public AsyncVehicleAssignmentListener(JpaDeadLetterRepository deadLetterRepository) {
        this.deadLetterRepository = deadLetterRepository;
    }

    @EventListener
    public void on(VehicleAssignedToFleetEvent event) {
        if (circuitOpen && System.currentTimeMillis() < openUntil) return;
        int attempts = 0;
        long backoff = 100;
        while (attempts < 3) {
            attempts++;
            try {
                log.info("async_dispatch tenant={} correlationId={} useCase=AssignVehicleToFleet vehicle={} fleet={}", event.getTenantId(), event.getCorrelationId(), event.getVehicleId(), event.getFleetId());
                failureCount = 0;
                circuitOpen = false;
                return;
            } catch (Exception ex) {
                failureCount++;
                try { Thread.sleep(backoff); } catch (InterruptedException ignored) {}
                backoff *= 2;
            }
        }
        if (failureCount >= 3) {
            circuitOpen = true;
            openUntil = System.currentTimeMillis() + 1000;
            DeadLetterEvent dl = new DeadLetterEvent();
            dl.setEventType(VehicleAssignedToFleetEvent.class.getName());
            dl.setPayload(serialize(event));
            dl.setCorrelationId(event.getCorrelationId());
            dl.setErrorMessage("dispatch_failed");
            deadLetterRepository.save(dl);
        }
    }

    private String serialize(Object o) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(o);
        } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
            return "{}";
        }
    }
}

