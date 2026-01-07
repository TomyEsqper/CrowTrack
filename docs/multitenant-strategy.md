# Multi-Tenant Data Strategy

Estrategia actual: base de datos compartida con columna tenant_id y filtros Hibernate.

Justificación: simplicidad operativa, menor coste y facilidad de onboarding.

Limitaciones: contención en recursos, ruido de datos y complejidad de aislamiento lógico.

Plan futuro: migración gradual a schema-per-tenant o db-per-tenant para clientes con mayor escala y requisitos de aislamiento, usando herramientas de migración automatizadas y federation.

