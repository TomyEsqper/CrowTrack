package com.tuplataforma.core.application.read;

import com.tuplataforma.core.infrastructure.persistence.fleet.JpaVehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class VehicleReadService {
    private final JpaVehicleRepository repo;
    public VehicleReadService(JpaVehicleRepository repo) { this.repo = repo; }
    public VehicleReadDto get(UUID id) {
        var e = repo.findById(id).orElseThrow(() -> new com.tuplataforma.core.domain.fleet.exceptions.VehicleNotFoundException(id));
        UUID fleetId = e.getFleet() != null ? e.getFleet().getId() : null;
        return new VehicleReadDto(e.getId(), e.getLicensePlate(), e.getModel(), e.isActive(), fleetId);
    }
    public List<VehicleReadDto> listAll() {
        return repo.findAll().stream().map(e -> new VehicleReadDto(e.getId(), e.getLicensePlate(), e.getModel(), e.isActive(), e.getFleet() != null ? e.getFleet().getId() : null)).toList();
    }
}

