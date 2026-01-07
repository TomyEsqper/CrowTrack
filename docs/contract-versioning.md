# Contract Stability and Versioning

API versionada explícitamente bajo prefijo /api/v1. Los DTOs son inmutables y podrán incluir campo de versión cuando se requiera. La estrategia de deprecación consiste en introducir /api/v2 en paralelo, mantener v1 hasta agotar clientes y anunciar retiradas con anticipación. No se rompen contratos existentes.

