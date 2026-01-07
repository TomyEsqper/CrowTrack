package com.tuplataforma.core.shared.correlation;

public class CorrelationContext {
    private static final ThreadLocal<String> CID = new ThreadLocal<>();
    public static void set(String id) { CID.set(id); }
    public static String get() { return CID.get(); }
    public static void clear() { CID.remove(); }
}

