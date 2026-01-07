CREATE TABLE companies (
    id UUID NOT NULL,
    tenant_id VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    active BOOLEAN NOT NULL,
    timezone VARCHAR(100),
    language VARCHAR(50),
    PRIMARY KEY (id)
);

CREATE INDEX idx_company_tenant ON companies(tenant_id);
