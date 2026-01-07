package com.tuplataforma.core.infrastructure.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tuplataforma.core.domain.events.DomainEvent;
import org.springframework.stereotype.Component;

@Component
public class JacksonDomainEventSerializer implements DomainEventSerializer {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String serialize(DomainEvent event) {
        try {
            return objectMapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Cannot serialize domain event", e);
        }
    }

    @Override
    public String eventType(DomainEvent event) {
        return event.getClass().getName();
    }

    @Override
    public String version(DomainEvent event) {
        return "1";
    }
}

