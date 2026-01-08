# ARCHITECTURE_GUARD

- Capas:
  - Dominio: sin dependencias de frameworks
  - Aplicación: orquesta casos de uso con guards obligatorios
  - Infraestructura: persistencia, web, seguridad, auditoría
- Intocable:
  - Reglas de tenant, permisos y suscripción
  - Separación read/write conceptual
  - Auditoría financiera y enterprise
- Cambios que requieren ADR:
  - Nuevos módulos, alteración de contratos, cambios en persistencia
  - Integraciones externas y seguridad
- La arquitectura se defiende sola:
  - Tests de arquitectura con ArchUnit
  - PMD/CPD para complejidad y duplicación

