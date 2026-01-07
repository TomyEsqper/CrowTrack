package com.tuplataforma.core.domain.fleet;

import com.tuplataforma.core.domain.events.VehicleAssignedToFleetEvent;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class VehicleDomainEventsTest {

    @Test
    void agregaEventoAlAsignar() {
        TenantId tenant = new TenantId("t-1");
        Vehicle v = new Vehicle(UUID.randomUUID(), tenant, "PLT-123", "X", true, null, null);
        Fleet f = new Fleet(UUID.randomUUID(), tenant, "Main", true);

        v.assignTo(f);
        assertEquals(1, v.collectDomainEvents().size());
        assertTrue(v.collectDomainEvents().get(0) instanceof VehicleAssignedToFleetEvent);
        VehicleAssignedToFleetEvent evt = (VehicleAssignedToFleetEvent) v.collectDomainEvents().get(0);
        assertEquals(v.getId(), evt.getVehicleId());
        assertEquals(f.getId(), evt.getFleetId());
        assertEquals(tenant.getValue(), evt.getTenantId());
        v.clearDomainEvents();
        assertEquals(0, v.collectDomainEvents().size());
    }
}

