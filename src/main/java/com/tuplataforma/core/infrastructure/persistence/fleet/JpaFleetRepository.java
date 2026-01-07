package com.tuplataforma.core.infrastructure.persistence.fleet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaFleetRepository extends JpaRepository<FleetEntity, UUID> {
}

