package com.tuplataforma.core.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class CreateTenantRequest {
    @NotBlank(message = "Tenant ID is required")
    @Pattern(regexp = "^[a-z0-9-]+$", message = "Tenant ID must be lowercase alphanumeric with hyphens")
    private String id;

    @NotBlank(message = "Name is required")
    private String name;
}
