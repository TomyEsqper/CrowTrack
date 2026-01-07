package com.tuplataforma.core.compatibility;

import com.tuplataforma.core.application.fleet.dto.VehicleResult;
import com.tuplataforma.core.application.fleet.dto.AssignmentResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DtoCompatibilityTest {
    @Test
    void vehicleResult_hasStableFields() throws Exception {
        VehicleResult vr = new VehicleResult(java.util.UUID.randomUUID(), "X", "M", true);
        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(vr);
        assertTrue(json.contains("licensePlate"));
        assertTrue(json.contains("model"));
        assertTrue(json.contains("active"));
        assertTrue(json.contains("id"));
    }
    @Test
    void assignmentResult_hasStableFields() throws Exception {
        AssignmentResult ar = new AssignmentResult(java.util.UUID.randomUUID(), java.util.UUID.randomUUID());
        String json = new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(ar);
        assertTrue(json.contains("vehicleId"));
        assertTrue(json.contains("fleetId"));
    }
}
