package com.tuplataforma.core.domain.model;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class Tenant {
    private String id;
    private String name;
    private String status;
    private Instant createdAt;

    public void activate() {
        this.status = "ACTIVE";
    }

    public void deactivate() {
        this.status = "INACTIVE";
    }
}
