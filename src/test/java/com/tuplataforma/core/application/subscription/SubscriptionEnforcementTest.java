package com.tuplataforma.core.application.subscription;

import com.tuplataforma.core.application.fleet.CreateVehicleService;
import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.application.governance.QuotaExceededException;
import com.tuplataforma.core.application.security.PermissionDeniedException;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class SubscriptionEnforcementTest {

    @Autowired
    private CreateVehicleService createVehicleService;

    @Autowired
    private ChangePlanUseCase changePlanUseCase;

    @Test
    void usoDentroDeLimite_ok() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"))));
        TenantContext.setTenantId(new TenantId("tenant-free"));
        changePlanUseCase.upgradeNow(TenantContext.getTenantId(), "FREE");
        var r = createVehicleService.execute(new CreateVehicleCommand("FREE-001", "M"));
        assertNotNull(r.id());
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }

    @Test
    void excesoDeCuota_bloqueado() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u","p",
                java.util.List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"))));
        TenantContext.setTenantId(new TenantId("tenant-free-2"));
        changePlanUseCase.upgradeNow(TenantContext.getTenantId(), "FREE");
        for (int i=0;i<5;i++) {
            createVehicleService.execute(new CreateVehicleCommand("F2-"+i, "M"));
        }
        assertThrows(QuotaExceededException.class, () ->
                createVehicleService.execute(new CreateVehicleCommand("F2-EXTRA", "M")));
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }
}

