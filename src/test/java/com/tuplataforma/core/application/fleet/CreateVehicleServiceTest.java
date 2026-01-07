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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

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
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p", java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"))));
        VehicleResult result = createVehicleService.execute(new CreateVehicleCommand("ABC-123", "ModelX"));
        assertNotNull(result.id());
        assertEquals("ABC-123", result.licensePlate());
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void violacionReglaDominio_matriculaInvalida() {
        TenantContext.setTenantId(new TenantId("tenant-x"));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p", java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"))));
        assertThrows(InvalidLicensePlateException.class, () ->
                createVehicleService.execute(new CreateVehicleCommand("  ", "ModelX"))
        );
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void aislamientoTenant_enUseCase() {
        TenantContext.setTenantId(new TenantId("tenant-a"));
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p", java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"))));
        VehicleResult a = createVehicleService.execute(new CreateVehicleCommand("TEN-001", "ModelA"));
        SecurityContextHolder.clearContext();
        TenantContext.clear();

        TenantContext.setTenantId(new TenantId("tenant-b"));
        assertThrows(RuntimeException.class, () -> getVehicleService.execute(a.id()));
        TenantContext.clear();
    }
}

