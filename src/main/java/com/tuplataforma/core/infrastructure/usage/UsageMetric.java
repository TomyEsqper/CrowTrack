package com.tuplataforma.core.infrastructure.usage;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "usage_metrics", indexes = {
        @Index(name = "idx_usage_tenant_period_type", columnList = "tenant_id, period, type", unique = true)
})
public class UsageMetric extends BaseTenantEntity {
    @Column(name = "period", nullable = false)
    private String period;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "count", nullable = false)
    private long count;
    public String getPeriod() { return period; }
    public String getType() { return type; }
    public long getCount() { return count; }
    public void setPeriod(String period) { this.period = period; }
    public void setType(String type) { this.type = type; }
    public void setCount(long count) { this.count = count; }
}

