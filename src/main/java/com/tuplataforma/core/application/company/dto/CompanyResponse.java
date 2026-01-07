package com.tuplataforma.core.application.company.dto;

import java.util.UUID;

public record CompanyResponse(
    UUID id,
    String tenantId,
    String name,
    boolean active,
    String timezone,
    String language
) {}
