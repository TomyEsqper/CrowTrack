package com.tuplataforma.core.application.governance;

import com.tuplataforma.core.shared.tenant.TenantStatus;

import java.util.Set;

public record TenantPolicy(TenantStatus status, Set<Module> enabledModules, int vehicleLimit, int fleetLimit, int userLimit) {}

