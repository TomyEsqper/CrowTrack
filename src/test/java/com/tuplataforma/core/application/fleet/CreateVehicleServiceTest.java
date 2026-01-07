package com.tuplataforma.core.application.fleet;

import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.application.fleet.dto.VehicleResult;
import com.tuplataforma.core.domain.fleet.exceptions.InvalidLicensePlateException;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class CreateVehicleServiceTest {

    @Autowired
    private CreateVehicleService createVehicleService;

    @Autowired
    private GetVehicleService getVehicleService;

    @Test
    void casoFeliz_creaVehiculo() {
        TenantContext.setTenantId(new TenantId("tenant-x"));
        VehicleResult result = createVehicleService.execute(new CreateVehicleCommand("ABC-123", "ModelX"));
        assertNotNull(result.id());
        assertEquals("ABC-123", result.licensePlate());
        TenantContext.clear();
    }

    @Test
    void violacionReglaDominio_matriculaInvalida() {
        TenantContext.setTenantId(new TenantId("tenant-x"));
        assertThrows(InvalidLicensePlateException.class, () ->
                createVehicleService.execute(new CreateVehicleCommand("  ", "ModelX"))
        );
        TenantContext.clear();
    }

    @Test
    void aislamientoTenant_enUseCase() {
        TenantContext.setTenantId(new TenantId("tenant-a"));
        VehicleResult a = createVehicleService.execute(new CreateVehicleCommand("TEN-001", "ModelA"));
        TenantContext.clear();

        TenantContext.setTenantId(new TenantId("tenant-b"));
        assertThrows(RuntimeException.class, () -> getVehicleService.execute(a.id()));
        TenantContext.clear();
    }
}

