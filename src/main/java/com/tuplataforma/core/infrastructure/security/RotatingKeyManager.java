package com.tuplataforma.core.infrastructure.security;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;

@Component
@Profile("!test")
public class RotatingKeyManager {
    private final Deque<KeyInfo> keys = new ArrayDeque<>();
    public RotatingKeyManager() {
        keys.addFirst(new KeyInfo("k1", Instant.now(), Instant.now().plusSeconds(3600)));
    }
    public KeyInfo current() { return keys.peekFirst(); }
    public void rotate() {
        String kid = "k" + (keys.size() + 1);
        keys.addFirst(new KeyInfo(kid, Instant.now(), Instant.now().plusSeconds(3600)));
        while (keys.size() > 3) keys.removeLast();
    }
    public record KeyInfo(String keyId, Instant createdAt, Instant expiresAt) {}
}

