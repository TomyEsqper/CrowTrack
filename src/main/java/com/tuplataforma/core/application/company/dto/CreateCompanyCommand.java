package com.tuplataforma.core.application.company.dto;

import java.util.Map;

public record CreateCompanyCommand(
    String name,
    String timezone,
    String language,
    Map<String, String> extraSettings
) {}
