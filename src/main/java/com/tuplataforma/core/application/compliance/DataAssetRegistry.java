package com.tuplataforma.core.application.compliance;

import java.util.Map;

public class DataAssetRegistry {
    public static Map<String, DataClassification> classifications() {
        return Map.of(
                "vehicles", DataClassification.OPERATIONAL,
                "fleets", DataClassification.OPERATIONAL,
                "usage_metrics", DataClassification.OPERATIONAL,
                "performance_metrics", DataClassification.OPERATIONAL,
                "dead_letter_events", DataClassification.OPERATIONAL,
                "finance_audit", DataClassification.FINANCIAL,
                "audit_trail", DataClassification.AUDIT
        );
    }
    public static Map<DataClassification, RetentionPolicy> retentionPolicies() {
        return Map.of(
                DataClassification.OPERATIONAL, new RetentionPolicy(365, true),
                DataClassification.AUDIT, new RetentionPolicy(1825, false),
                DataClassification.FINANCIAL, new RetentionPolicy(3650, false)
        );
    }
}

