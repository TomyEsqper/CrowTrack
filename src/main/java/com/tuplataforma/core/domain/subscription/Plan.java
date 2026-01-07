package com.tuplataforma.core.domain.subscription;

import java.util.Set;

public record Plan(PlanCode code, Set<String> includedModules, int vehiclesLimit, int fleetsLimit, int usersLimit, int apiCallsLimit, long priceCents) {
    public boolean includesModule(String module) { return includedModules.contains(module); }
}

