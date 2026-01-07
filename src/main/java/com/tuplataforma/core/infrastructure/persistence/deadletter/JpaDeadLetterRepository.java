package com.tuplataforma.core.infrastructure.persistence.deadletter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaDeadLetterRepository extends JpaRepository<DeadLetterEvent, UUID> {
}

