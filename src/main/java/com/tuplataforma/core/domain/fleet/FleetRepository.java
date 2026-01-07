package com.tuplataforma.core.domain.fleet;

import java.util.Optional;
import java.util.UUID;

public interface FleetRepository {
    Optional<Fleet> findById(UUID id);
}

