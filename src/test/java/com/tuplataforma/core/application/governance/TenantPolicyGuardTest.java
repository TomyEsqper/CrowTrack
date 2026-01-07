package com.tuplataforma.core.application.governance;

import com.tuplataforma.core.application.fleet.CreateVehicleService;
import com.tuplataforma.core.application.fleet.dto.CreateVehicleCommand;
import com.tuplataforma.core.shared.tenant.TenantContext;
import com.tuplataforma.core.shared.tenant.TenantId;
import com.tuplataforma.core.shared.tenant.TenantStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TenantPolicyGuardTest {

    @Autowired
    private CreateVehicleService createVehicleService;
    @Autowired
    private InMemoryTenantPolicyProvider inMemoryTenantPolicyProvider;

    @Test
    void tenantSuspendido_noPermiteUseCase() {
        inMemoryTenantPolicyProvider.setPolicy("tenant-s", new TenantPolicy(TenantStatus.SUSPENDED, Set.of(Module.VEHICLE, Module.FLEET), Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
        TenantContext.setTenantId(new TenantId("tenant-s"));
        assertThrows(TenantSuspendedException.class, () -> createVehicleService.execute(new CreateVehicleCommand("HARD-10", "Secure")));
        TenantContext.clear();
    }
}

