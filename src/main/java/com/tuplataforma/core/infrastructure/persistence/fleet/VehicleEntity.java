package com.tuplataforma.core.infrastructure.persistence.fleet;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "vehicles", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"tenant_id", "license_plate"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VehicleEntity extends BaseTenantEntity {

    @Column(name = "license_plate", nullable = false)
    private String licensePlate;

    @Column(name = "model", nullable = false)
    private String model;

    @Column(name = "active", nullable = false)
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "fleet_id")
    private FleetEntity fleet;
}
