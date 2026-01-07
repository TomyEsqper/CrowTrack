# Plataforma SaaS Multi‑Tenant de Transporte Público — Visión y Definición

## 1. Naturaleza del producto
- Plataforma SaaS B2B crítica, multi‑tenant, con separación estricta de datos.
- Control centralizado y escalabilidad técnica/operativa progresiva.
- Soporta múltiples empresas en paralelo con aislamiento total.
- Integración con tracking GPS en tiempo real.
- Infraestructura profesional desde el día uno (no un MVP).

## 2. Arquitectura por planos (no negociable)
- Control Plane (plataforma central):
  - No pertenece a ningún tenant; operado por la empresa dueña.
  - Funciones de gobernanza del sistema completo.
- Tenant Plane (empresas/tenants):
  - Cada empresa es un tenant; sin cruce de datos.
  - Cada tenant ve solo su universo.

## 3. Stack tecnológico
- Core de negocio:
  - Java 21, Spring Boot 3, arquitectura hexagonal (Ports & Adapters).
  - Monolito modular con PostgreSQL, Hibernate y migraciones Flyway.
- Servicio GPS (externo al core):
  - Node.js + TypeScript.
  - Responsabilidad: ingesta y normalización de eventos GPS.
  - Comunicación con el core por eventos/API interna, sin compartir base de datos.
- Decisión clave:
  - No microservicios por ahora; monolito modular preparado para extraer módulos.

## 4. Arquitectura hexagonal (aplicación real)
- Principios:
  - El dominio no depende de frameworks; Spring es infraestructura.
  - Reglas de negocio viven en el dominio.
- Estructura:
  - core/
    - domain/ (model, services, repositories interfaces, events)
    - application/ (usecases, ports, dto)
    - infrastructure/ (web, persistence, security, messaging, config)
    - bootstrap/
- Regla dura:
  - Si el dominio importa Spring, está mal hecho.

## 5. Multi‑tenancy (núcleo del sistema)
- Modelo:
  - Un solo sistema/back‑end, aislamiento lógico por tenant_id.
- Reglas:
  - Toda entidad tenant‑aware incluye tenant_id.
  - El back‑end resuelve el tenant por token o subdominio (nunca el front).
  - Ninguna query se ejecuta sin contexto de tenant.
  - Fallo de aislamiento = fallo crítico del producto.

## 6. Plataforma central (Control Plane)
- Funciones:
  - Crear, activar y desactivar tenants; definir planes y límites.
  - Habilitar módulos por empresa; ver métricas globales.
  - Auditoría y soporte; facturación futura.
- Usuarios:
  - SUPER_ADMIN, PLATFORM_OPERATOR (no operan buses, rutas ni GPS).

## 7. Creación de un tenant (flujo real)
- No es un simple INSERT.
- Proceso:
  - Registrar empresa y plan; activar módulos; crear configuración inicial.
  - Definir límites; crear usuario administrador del tenant y credenciales.
  - Registrar auditoría.
- Caso de uso crítico y transaccional.

## 8. Módulos del dominio operativo (Tenant Plane)
- Ejemplos:
  - Fleet (vehículos), Routes, Schedules, Drivers, Users, GPS state, Alerts.
- Todos:
  - Tenant‑aware, protegidos por permisos, aislados.

## 9. Seguridad (sin concesiones)
- Autenticación:
  - JWT firmado (RS256) con emisores separados para plataforma y tenants.
  - Claims: user_id, tenant_id (si aplica), roles, scopes.
- Autorización:
  - RBAC + scopes, validación en back‑end; el front no decide permisos.

## 10. Rendimiento y asincronía
- Asíncrono donde corresponde:
  - Procesamiento GPS, notificaciones, auditoría, métricas.
- No asíncrono en:
  - Lógica crítica y flujos transaccionales centrales.

## 11. Integración GPS (modelo correcto)
- El core no consulta al GPS.
- Flujo:
  - Dispositivo → Servicio GPS (Node) → Evento → Core (Java).
- El core:
  - Valida, normaliza, persiste y actualiza estado.
- Eventos:
  - Pueden llegar tarde, duplicados o corruptos; el sistema debe resistirlos.

## 12. Gobernanza y ciclo de vida del tenant
- Capacidades:
  - Exportar datos por tenant; desactivar; borrar o anonimizar datos.
  - Restaurar backups por tenant.
- Si no se puede borrar un tenant completo, existe un problema legal/técnico.

## 13. Observabilidad (desde el día uno)
- Logs estructurados con tenant_id y Correlation ID.
- Métricas por empresa y alertas.
- Sin observabilidad, no hay operación.

## 14. Principios finales
- Seguridad primero; aislamiento total; dominio limpio.
- Escalar sin reescribir; el back‑end manda.
- Nada “después vemos”.

## 15. Conclusión
- No es una app, CRUD ni experimento.
- Es una plataforma SaaS multi‑tenant de nivel empresarial.
- Cada decisión técnica impacta negocio, clientes y legalidad.

# REGLAS DE INGENIERÍA DEL PROYECTO 
 
 **(Obligatorias, no negociables)** 
 
 ## 1. Regla cero 
 
 **Código que “funciona” pero no es mantenible es código roto.** 
 
 No se acepta: 
 
 * “Después lo limpiamos” 
 * “Es solo un MVP” 
 * “Así se hace más rápido” 
 
 Eso destruye plataformas. 
 
 --- 
 
 ## 2. Definición formal de “código basura” 
 
 Se considera **código basura** todo lo que cumpla UNA de estas: 
 
 * No tiene dueño lógico (no se sabe por qué existe). 
 * Duplica lógica ya existente. 
 * Mezcla responsabilidades. 
 * No respeta límites de capa. 
 * Depende del framework sin necesidad. 
 * No se puede testear sin levantar medio sistema. 
 * Vive “por si acaso”. 
 
 Código no usado = **código muerto** → se elimina. 
 
 --- 
 
 ## 3. DRY (bien entendido, no fanático) 
 
 ### DRY correcto 
 
 * Lógica de negocio vive **una sola vez**. 
 * Reglas no se copian entre casos de uso. 
 * Validaciones críticas están centralizadas. 
 
 ### DRY incorrecto 
 
 * Crear abstracciones prematuras. 
 * Utils gigantes compartidas sin cohesión. 
 * “Helper” que hace de todo. 
 
 **Si abstraer hace el código más confuso, no es DRY, es overengineering.** 
 
 --- 
 
 ## 4. Single Responsibility (aplicado de verdad) 
 
 Cada clase: 
 
 * Tiene **una razón para cambiar**. 
 * Se puede explicar en una frase. 
 
 Si necesitas “y además” → clase mal diseñada. 
 
 --- 
 
 ## 5. Prohibiciones explícitas 
 
 ### NO se permite: 
 
 * Lógica de negocio en controllers. 
 * Reglas en el frontend. 
 * Repositorios con lógica compleja. 
 * Servicios “god”. 
 * Utils genéricas sin dominio. 
 * Copiar/pegar entre módulos. 
 * Comentarios explicando código mal escrito. 
 
 Si necesitas comentar mucho, reescribe. 
 
 --- 
 
 ## 6. Organización de archivos (higiene) 
 
 ### Regla 
 
 **Si un archivo no pertenece claramente a un módulo, sobra.** 
 
 No: 
 
 * paquetes `misc` 
 * paquetes `common` infinitos 
 * clases “temporales” 
 
 Cada archivo debe: 
 
 * pertenecer a un dominio 
 * tener nombre específico 
 * justificar su existencia 
 
 --- 
 
 ## 7. Nombres (disciplina extrema) 
 
 * Nombres explícitos > cortos. 
 * No abreviaturas crípticas. 
 * No siglas internas inventadas. 
 
 Ejemplo malo: 
 
 ``` 
 SvcUtils 
 ``` 
 
 Ejemplo bueno: 
 
 ``` 
 TenantProvisioningService 
 ``` 
 
 El nombre debe explicar **qué problema resuelve**. 
 
 --- 
 
 ## 8. Límite de tamaño (regla dura) 
 
 * Clases: máximo ~300 líneas. 
 * Métodos: máximo ~30 líneas. 
 * Más grande = dividir. 
 
 Código largo no es código potente. 
 Es código difícil. 
 
 --- 
 
 ## 9. Manejo de errores 
 
 ### Regla 
 
 Los errores: 
 
 * No se silencian. 
 * No se capturan “por si acaso”. 
 * No se exponen crudos al cliente. 
 
 Errores son parte del diseño, no excepciones accidentales. 
 
 --- 
 
 ## 10. Eliminación activa de basura 
 
 Cada iteración: 
 
 * se elimina código no usado 
 * se borran flags muertos 
 * se quitan endpoints obsoletos 
 
 No se acumula basura “por si acaso”. 
 
 Git existe. El código muerto no se guarda. 
 
 --- 
 
 ## 11. Tests (realistas, no teatro) 
 
 Se testea: 
 
 * lógica de negocio 
 * casos críticos 
 * validaciones multi-tenant 
 * seguridad 
 
 No se testea: 
 
 * getters/setters 
 * frameworks 
 * código trivial 
 
 Test que no detecta fallos = ruido. 
 
 --- 
 
 ## 12. Evolución consciente 
 
 Antes de agregar: 
 
 * ¿esto pertenece aquí? 
 * ¿rompe aislamiento? 
 * ¿duplica concepto? 
 * ¿se puede extraer después? 
 
 Diseñar pensando en extracción futura **sin anticiparla en exceso**. 
 
 --- 
 
 ## 13. Reglas de dependencia 
 
 * Dominio no depende de infraestructura. 
 * Infraestructura depende del dominio. 
 * No se rompe el flujo por comodidad. 
 
 Si una dependencia es “más fácil”, pero rompe arquitectura: 
 NO se usa. 
 
 --- 
 
 ## 14. Performance ≠ hacks 
 
 No se aceptan: 
 
 * caches mágicos 
 * optimizaciones sin medición 
 * async indiscriminado 
 
 Primero: 
 
 * claridad 
 * corrección 
 * observabilidad 
 
 Luego: 
 
 * optimización informada 
 
 --- 
 
 ## 15. Regla final (la más importante) 
 
 **El código es un activo de largo plazo, no un costo a minimizar.** 
 
 Cada línea: 
 
 * se lee más veces de las que se escribe 
 * vive más que quien la escribió 
 * tiene impacto real 
 
 Código basura es deuda. 
 Deuda en SaaS crítico se paga con clientes.
