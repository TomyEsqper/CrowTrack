package com.tuplataforma.core.application.identity.dto;

public record LoginCommand(
    String email,
    String password
) {}
