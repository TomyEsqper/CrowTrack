package com.tuplataforma.core.application.idempotency;

public class IdempotencyContext {
    private static final ThreadLocal<String> KEY = new ThreadLocal<>();
    public static void set(String key) { KEY.set(key); }
    public static String get() { return KEY.get(); }
    public static void clear() { KEY.remove(); }
}

