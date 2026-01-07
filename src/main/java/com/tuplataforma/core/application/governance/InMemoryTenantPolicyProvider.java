package com.tuplataforma.core.application.governance;

import com.tuplataforma.core.shared.tenant.TenantId;
import com.tuplataforma.core.shared.tenant.TenantStatus;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryTenantPolicyProvider implements TenantPolicyProvider {

    private final Map<String, TenantPolicy> policies = new ConcurrentHashMap<>();
    private final Map<String, Integer> vehicles = new ConcurrentHashMap<>();
    private final Map<String, Integer> fleets = new ConcurrentHashMap<>();
    private final Map<String, Integer> users = new ConcurrentHashMap<>();

    public InMemoryTenantPolicyProvider() {
        policies.putIfAbsent("default", new TenantPolicy(TenantStatus.ACTIVE, Set.of(Module.VEHICLE, Module.FLEET), Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE));
    }

    public void setPolicy(String tenantId, TenantPolicy policy) {
        policies.put(tenantId, policy);
    }

    public void setCounts(String tenantId, int vehicleCount, int fleetCount, int userCount) {
        vehicles.put(tenantId, vehicleCount);
        fleets.put(tenantId, fleetCount);
        users.put(tenantId, userCount);
    }

    @Override
    public TenantPolicy policyFor(TenantId tenantId) {
        return policies.getOrDefault(tenantId.getValue(), policies.get("default"));
    }

    @Override
    public int currentVehicleCount(TenantId tenantId) {
        return vehicles.getOrDefault(tenantId.getValue(), 0);
    }

    @Override
    public int currentFleetCount(TenantId tenantId) {
        return fleets.getOrDefault(tenantId.getValue(), 0);
    }

    @Override
    public int currentUserCount(TenantId tenantId) {
        return users.getOrDefault(tenantId.getValue(), 0);
    }
}

