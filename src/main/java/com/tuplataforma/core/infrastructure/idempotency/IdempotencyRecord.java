package com.tuplataforma.core.infrastructure.idempotency;

import com.tuplataforma.core.infrastructure.persistence.shared.BaseTenantEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "idempotency_records", indexes = {
        @Index(name = "idx_idem_unique", columnList = "tenant_id, idempotency_key, use_case", unique = true)
})
public class IdempotencyRecord extends BaseTenantEntity {

    @Column(name = "idempotency_key", nullable = false)
    private String idempotencyKey;

    @Column(name = "use_case", nullable = false)
    private String useCase;

    @Column(name = "result_payload", nullable = false, columnDefinition = "TEXT")
    private String resultPayload;

    public String getIdempotencyKey() { return idempotencyKey; }
    public String getUseCase() { return useCase; }
    public String getResultPayload() { return resultPayload; }

    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    public void setUseCase(String useCase) { this.useCase = useCase; }
    public void setResultPayload(String resultPayload) { this.resultPayload = resultPayload; }
}

