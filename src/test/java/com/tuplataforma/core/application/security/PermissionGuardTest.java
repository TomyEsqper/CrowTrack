package com.tuplataforma.core.application.security;

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
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class PermissionGuardTest {

    @Autowired
    private CreateVehicleService createVehicleService;
    @Autowired
    private AssignVehicleToFleetService assignVehicleToFleetService;
    @Autowired
    private JpaFleetRepository jpaFleetRepository;

    @TestConfiguration
    static class OverrideResolver {
        @Bean
        @Primary
        public PermissionResolver permissionResolver() {
            return () -> {
                var auth = SecurityContextHolder.getContext().getAuthentication();
                java.util.Set<Permission> set = new java.util.HashSet<>();
                if (auth != null) {
                    auth.getAuthorities().forEach(a -> {
                        try { set.add(Permission.valueOf(a.getAuthority())); } catch (IllegalArgumentException ignored) {}
                    });
                }
                return set;
            };
        }
    }
    @Test
    void sinPermiso_vehicleAssign_falla() {
        SecurityContextHolder.getContext().setAuthentication(new TestingAuthenticationToken("u", "p", List.of(new SimpleGrantedAuthority("VEHICLE_CREATE"))));
        TenantContext.setTenantId(new TenantId("tenant-p"));
        FleetEntity fleet = new FleetEntity();
        fleet.setName("P");
        fleet.setActive(true);
        FleetEntity f = jpaFleetRepository.save(fleet);
        var v = createVehicleService.execute(new CreateVehicleCommand("PERM-1", "M"));
        assertThrows(PermissionDeniedException.class, () -> assignVehicleToFleetService.execute(new AssignVehicleToFleetCommand(v.id(), f.getId())));
        SecurityContextHolder.clearContext();
        TenantContext.clear();
    }
}

