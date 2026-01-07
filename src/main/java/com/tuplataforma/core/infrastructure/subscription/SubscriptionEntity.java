package com.tuplataforma.core.infrastructure.subscription;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "subscriptions")
public class SubscriptionEntity extends BaseTenantEntity {
    @Column(name = "plan_code", nullable = false)
    private String planCode;
    @Column(name = "status", nullable = false)
    private String status;
    @Column(name = "start_date", nullable = false)
    private java.time.LocalDate startDate;
    @Column(name = "renewal_date", nullable = false)
    private java.time.LocalDate renewalDate;
    public String getPlanCode() { return planCode; }
    public String getStatus() { return status; }
    public java.time.LocalDate getStartDate() { return startDate; }
    public java.time.LocalDate getRenewalDate() { return renewalDate; }
    public void setPlanCode(String planCode) { this.planCode = planCode; }
    public void setStatus(String status) { this.status = status; }
    public void setStartDate(java.time.LocalDate startDate) { this.startDate = startDate; }
    public void setRenewalDate(java.time.LocalDate renewalDate) { this.renewalDate = renewalDate; }
}

