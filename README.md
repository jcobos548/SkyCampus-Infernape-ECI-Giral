# SkyCampus-Infernape-ECI-Giral

## Repositorio Infernape: 

https://github.com/jcobos548/SkyCampus-Infernape-ECI-Giral.git

# Evidencia del Reto 1 — Infernape

## Captura de evidencia

![Evidencia del Reto 1](docs/evidencias/Reto1.png)

# Evidencia del Reto 2 — Infernape

![Evidencia del Reto 2](docs/evidencias/Reto2.png)

# Reto 3 — Nuevos problemas, nuevos patrones

## 1. Composite — rutas simples y compuestas
```mermaid
classDiagram
 class Ruta {
   <<interface>>
   +calcularDistanciaKm() double
   +ejecutar() void
   +getCantidadEtapas() int
 }
 class RutaSimple
 class RutaCompuesta {
   -etapas List~Ruta~
   +agregarEtapa(Ruta) void
   +eliminarEtapa(Ruta) boolean
 }
 Ruta <|.. RutaSimple
 Ruta <|.. RutaCompuesta
 RutaCompuesta o--> Ruta : contiene etapas
```
**Justificación:** permite tratar una ruta simple y una compuesta mediante la misma interfaz. Una solución con condicionales o clases para cada cantidad de etapas haría que el gestor tuviera que cambiar cuando cambie la estructura.

## 2. Strategy — optimización intercambiable
```mermaid
classDiagram
 class EstrategiaOptimizacion {
   <<interface>>
   +calcularTiempoMinutos(distanciaKm) double
 }
 class OptimizacionRapida
 class OptimizacionEnergetica
 class RutaSimple
 EstrategiaOptimizacion <|.. OptimizacionRapida
 EstrategiaOptimizacion <|.. OptimizacionEnergetica
 RutaSimple --> EstrategiaOptimizacion : delega cálculo
```
**Justificación:** cada algoritmo se encapsula detrás de una interfaz común. Es preferible a una cadena de `if/else` dentro de `RutaSimple`: se pueden agregar algoritmos sin modificar la clase existente, respetando el principio abierto/cerrado. En este ejemplo, rápida usa 60 km/h y energética 40 km/h.

## 3. Observer — alertas por etapa
```mermaid
classDiagram
 class ObservadorEtapa {
   <<interface>>
   +actualizar(mensaje) void
 }
 class AlertaEtapa
 class RutaSimple {
   -observadores List~ObservadorEtapa~
   +agregarObservador(ObservadorEtapa) void
 }
 ObservadorEtapa <|.. AlertaEtapa
 RutaSimple o--> ObservadorEtapa : notifica eventos
```
**Justificación:** separa la emisión de eventos de las acciones que reaccionan. Se pueden añadir receptores de consola, logs o monitoreo sin cambiar la ruta. Imprimir las alertas directamente en toda la lógica acoplaría el dominio a un único canal.

## 4. Factory Method — creación del tipo de dron
```mermaid
classDiagram
 class FabricaDrone {
   <<abstract>>
   +crearDrone() Drone
 }
 class FabricaDroneCarga
 class FabricaDroneRapido
 class Drone {
   <<interface>>
   +volar(origen, destino, distanciaKm, tiempoMinutos) void
 }
 class DroneCarga
 class DroneRapido
 FabricaDrone <|-- FabricaDroneCarga
 FabricaDrone <|-- FabricaDroneRapido
 Drone <|.. DroneCarga
 Drone <|.. DroneRapido
 FabricaDroneCarga ..> DroneCarga : crea
 FabricaDroneRapido ..> DroneRapido : crea
```
**Justificación:** la ruta solicita un `Drone` mediante una fábrica abstracta, sin instanciar directamente clases concretas. Nuevos tipos se agregan con nuevas fábricas, en vez de ampliar condicionales dentro de `RutaSimple`.

## Diagrama

![Diagramas del Reto 3](docs/diagramas/UMLPatrones.png)

# Reto 4 — Asignador de Misiones Enterprise

## Evidencia

![Evidencia del Reto 4](docs/evidencias/Reto4.png)

# Reto 5 — Evolución de los diagramas de contexto de SkyCampus

## 1. Introducción

Los diagramas de contexto de SkyCampus muestran cómo ha evolucionado el sistema desde su versión inicial MVP (Chimchar), pasando por la versión v2 (Monferno), hasta llegar a Enterprise (Infernape). En cada etapa se amplían los actores, las integraciones y las responsabilidades, de acuerdo con las necesidades de una plataforma cada vez más completa.

## 2. Comparación de los tres diagramas

### Chimchar — MVP

En la primera versión, SkyCampus App tiene una estructura sencilla y se centra en gestionar solicitudes y misiones de entrega con drones en el campus.

Los actores que interactúan con el sistema son:

- **Solicitante:** registra las solicitudes de entrega.
- **Operador:** gestiona las operaciones.
- **Admin:** administra el sistema.

En esta etapa no aparecen sistemas externos. Esto permite concentrarse en las funcionalidades principales antes de incorporar nuevas integraciones.

![Diagrama del Reto 5](docs/diagramas/DC1.png)

### Monferno — v2

En la segunda versión, SkyCampus conserva su propósito principal, pero amplía sus relaciones con el entorno. Aparecen integraciones con la API Meteorológica, el Control Aéreo ECI y el Sistema de Alertas.

También se amplía la representación de los actores administrativos y operativos. El diagrama muestra al Operador, Solicitante y Admin, con una segunda representación de Admin, además de las conexiones con los sistemas externos.

Esta evolución permite contemplar información meteorológica, coordinación aérea y notificaciones, haciendo que el sistema dependa de más componentes externos que en el MVP.

![Diagrama del Reto 5](docs/diagramas/DC2.png)

### Infernape — Enterprise

En la versión Enterprise, SkyCampus incorpora nuevos roles y amplía sus integraciones institucionales.

A los actores anteriores se suman:

- **Superadmin:** representa la supervisión general de la plataforma.
- **Coordinador de sede:** representa la coordinación de las operaciones en una sede.
- **Técnico de Mantenimiento:** representa las actividades de mantenimiento de los drones.

En cuanto a los sistemas externos, el diagrama incluye Aerocivil, ERP Universitario, Control Aéreo ECI, API Meteorológica y Sistema de Alertas. Esto refleja un entorno más amplio, con necesidades de coordinación institucional, operación y mantenimiento.

![Diagrama del Reto 5](docs/diagramas/DC3.png)

## 3. ¿Qué creció?

El crecimiento se observa principalmente en tres aspectos:

1. **Actores y responsabilidades:** aparecen roles relacionados con la supervisión empresarial, la coordinación de sedes y el mantenimiento de los drones.
2. **Integraciones externas:** se pasa de no tener sistemas externos en el MVP a depender de servicios meteorológicos, control aéreo, alertas e integraciones institucionales en las versiones posteriores.
3. **Complejidad operativa:** SkyCampus debe coordinar más responsabilidades y relaciones, lo que exige una mejor organización de sus funcionalidades y de la comunicación con otros sistemas.

## 4. ¿Qué se mantuvo?

A pesar de los cambios, el propósito central de SkyCampus sigue siendo el mismo: gestionar solicitudes y misiones de entrega con drones en el campus.

También se mantienen los actores fundamentales de la primera versión: Solicitante, Operador y Admin. Estos siguen siendo importantes incluso cuando se agregan nuevas responsabilidades y sistemas externos.

La aplicación continúa representando el punto central de interacción entre los usuarios y las funcionalidades del sistema. Las nuevas capacidades amplían su alcance, pero no reemplazan su objetivo inicial.

## 5. ¿Cómo evolucionó la complejidad sin perder la coherencia?

La complejidad aumentó de manera progresiva. Chimchar comienza con una aplicación sencilla y sin integraciones externas. Monferno incorpora servicios que apoyan la operación, como la información meteorológica, el control aéreo y las alertas. Finalmente, Infernape amplía los perfiles de usuario y las integraciones institucionales.

La coherencia se conserva porque las nuevas capacidades siguen relacionadas con el objetivo original de SkyCampus. No se cambia el propósito del sistema; se agregan responsabilidades y colaboraciones que permiten responder a necesidades más amplias.

Esta evolución también demuestra la importancia de definir claramente las responsabilidades de cada actor y de cada sistema externo. A medida que aumenta la complejidad, resulta necesario organizar mejor las relaciones para facilitar el mantenimiento, la comprensión y la futura ampliación de la plataforma.

## 6. Conclusión

La comparación de los tres diagramas muestra que SkyCampus pasó de una solución básica centrada en las solicitudes de entrega a una plataforma con más roles y relaciones institucionales.

Lo que más creció fue el número de responsabilidades y de integraciones externas, mientras que se mantuvo el objetivo principal de gestionar las operaciones de reparto con drones. La evolución progresiva permitió ampliar el sistema sin perder su identidad ni su propósito.

# Reto 6 — Matriz de trazabilidad de SkyCampus Enterprise

## 1. Objetivo

Construir una matriz de trazabilidad que relacione los requisitos funcionales (RF) y no funcionales (RNF) de SkyCampus Enterprise con sus casos de uso (CU), historias de usuario (HU) registradas en Jira y pruebas de validación.

La trazabilidad permite verificar que cada requisito tenga una forma de implementación y validación, identificar inconsistencias y comprobar que las funcionalidades del sistema respeten las restricciones de seguridad.

## 2. Priorización MoSCoW

Se utiliza el método MoSCoW para clasificar la importancia de los requisitos:

- **Must have:** requisito indispensable para la entrega.
- **Should have:** requisito importante, pero no indispensable para el funcionamiento mínimo.
- **Could have:** funcionalidad conveniente que puede aplazarse.
- **Won't have:** funcionalidad excluida del alcance de esta entrega.

## 3. Historias de usuario registradas en Jira

Las siguientes historias corresponden al backlog de Infernape (Enterprise):

| Clave Jira | Historia de usuario |
|---|---|
| SKC-14 | Configurar radio máximo de vuelo por sede |
| SKC-15 | Validar restricciones aéreas antes de autorizar vuelos |
| SKC-16 | Asignar drones automáticamente a las misiones |
| SKC-17 | Consultar condiciones meteorológicas |
| SKC-18 | Generar alertas automáticas por fallos |
| SKC-19 | Gestionar técnicos de mantenimiento |
| SKC-20 | Visualizar la flota agrupada por estado |
| SKC-21 | Gestionar usuarios y permisos por rol |

Estas claves son las identificadas en el tablero de Jira. Los requisitos, casos de uso y pruebas que aparecen a continuación constituyen la propuesta de trazabilidad para relacionarlas.

## 4. Matriz de trazabilidad de requisitos funcionales

| Código | Requisito funcional | MoSCoW | Caso de uso | Historia Jira | Prueba de validación |
|---|---|---|---|---|---|
| RF-01 | Asignar drones automáticamente a las misiones | Must | CU-01: Asignar drones | SKC-16 | PR-01: Verificar que se asigne un dron apto y disponible, y que se rechace la asignación si no existe uno adecuado. |
| RF-02 | Consultar condiciones meteorológicas | Must | CU-02: Consultar meteorología | SKC-17 | PR-02: Verificar que se consulten y presenten las condiciones meteorológicas disponibles. |
| RF-03 | Generar alertas automáticas por fallos | Must | CU-03: Gestionar alertas | SKC-18 | PR-03: Simular un fallo y verificar que se genere la alerta correspondiente. |
| RF-04 | Gestionar técnicos de mantenimiento | Should | CU-04: Gestionar mantenimiento | SKC-19 | PR-04: Verificar el registro y la consulta de técnicos de mantenimiento. |
| RF-05 | Visualizar la flota agrupada por estado | Should | CU-05: Consultar flota | SKC-20 | PR-05: Verificar que los drones se agrupen de acuerdo con su estado actual. |
| RF-06 | Gestionar usuarios y permisos por rol | Must | CU-06: Administrar permisos | SKC-21 | PR-06: Verificar que cada rol pueda ejecutar únicamente las operaciones autorizadas. |
| RF-07 | Validar las restricciones aéreas antes de autorizar vuelos | Must | CU-07: Validar ruta de vuelo | SKC-15 | PR-07: Verificar que se rechacen las rutas que incumplan las restricciones aéreas aplicables. |
| RF-12 | Configurar el radio máximo de vuelo por sede | Must | CU-08: Configurar radio de vuelo | SKC-14 | PR-08: Verificar que se guarde una configuración válida y que esta no permita autorizar vuelos que incumplan restricciones de seguridad. |

**Nota:** RF-01 a RF-07 representan la numeración propuesta para organizar las funcionalidades del backlog. RF-12 se conserva porque corresponde al requisito explícito del reto. Los identificadores CU y PR son referencias documentales propuestas, no claves de Jira ni pruebas cuya implementación ya esté confirmada.

## 5. Matriz de trazabilidad de requisitos no funcionales

| Código | Requisito no funcional | Prioridad | Caso de uso relacionado | Historia Jira relacionada | Prueba de validación |
|---|---|---|---|---|---|
| RNF-01 | Seguridad y control de acceso por roles | Must | CU-06: Administrar permisos | SKC-21 | PR-09: Verificar que un usuario sin autorización no pueda ejecutar operaciones restringidas. |
| RNF-02 | Rendimiento de consultas y operaciones | Should | CU-02: Consultar meteorología | SKC-17 | PR-10: Medir el tiempo de respuesta frente a un umbral definido por el equipo. |
| RNF-03 | Disponibilidad y manejo controlado de fallos | Should | CU-03: Gestionar alertas | SKC-18 | PR-11: Simular una falla de una dependencia y verificar que el sistema responda de forma controlada. |
| RNF-09 | Cumplimiento de las restricciones del espacio aéreo controlado y del límite urbano de 120 m | Must | CU-07: Validar ruta de vuelo | SKC-15 | PR-12: Verificar que se rechace una ruta que supere los 120 m en una zona urbana, incluso si el radio configurado lo permite. |

**Importante:** RNF-09 es el requisito de seguridad aérea indicado en el reto. RNF-01, RNF-02 y RNF-03 son propuestas para completar los cuatro RNF solicitados y deben contrastarse con el catálogo oficial del proyecto. Para RNF-02 y RNF-03 es necesario acordar métricas medibles antes de ejecutar las pruebas.

## 6. Contradicción entre RF-12 y RNF-09

### Descripción del conflicto

RF-12 permite que el coordinador de cada sede configure el radio máximo de vuelo de los drones. Por su parte, RNF-09 establece que las rutas entre sedes deben respetar las restricciones del espacio aéreo controlado por Aerocivil y que ningún dron puede volar por encima de 120 metros en una zona urbana, según la condición definida para este proyecto.

El conflicto aparece cuando el radio configurado permite una ruta que incumple una restricción aérea. Por ejemplo, un coordinador podría configurar un radio de 10 km, pero una ruta dentro de ese alcance podría atravesar una zona restringida o superar el límite de altura urbana.

### Resolución

La configuración realizada por el coordinador no puede anular las restricciones de seguridad aérea.

- El coordinador puede configurar el radio máximo de vuelo de su sede dentro de los límites operativos permitidos.
- Antes de autorizar una misión, el sistema debe validar la ruta frente a las restricciones aéreas aplicables.
- Una ruta que supere los 120 metros en una zona urbana debe ser rechazada conforme al requisito del proyecto.
- Una ruta que atraviese una zona restringida debe rechazarse o modificarse hasta cumplir las restricciones aplicables.
- Si no es posible verificar una restricción crítica, el sistema no debe autorizar automáticamente la misión.

### Regla de decisión

**Una misión solo puede continuar si respeta el radio configurado y las restricciones aéreas aplicables.**

De manera conceptual:

`Ruta autorizada = dentro del radio permitido Y cumple las restricciones aéreas`

La expresión representa una regla lógica de diseño. No sustituye la validación de la ruta completa, la altitud, las zonas restringidas ni las autorizaciones necesarias.

### Pruebas de aceptación

| Prueba | Escenario | Resultado esperado |
|---|---|---|
| PR-12A | El radio está configurado correctamente y la ruta cumple las restricciones. | La ruta puede continuar con las demás validaciones. |
| PR-12B | El radio permite una ruta que supera los 120 m en zona urbana. | El sistema rechaza la ruta. |
| PR-12C | La ruta atraviesa una zona restringida. | El sistema rechaza la ruta o exige una alternativa válida. |
| PR-12D | No se puede verificar una restricción aérea crítica. | El sistema no autoriza automáticamente la misión. |

## 7. Otras contradicciones y riesgos

### 7.1. Asignación automática y mantenimiento

**Conflicto:** el sistema podría intentar asignar una misión a un dron que está en mantenimiento o fuera de servicio.

**Resolución:** la asignación automática debe comprobar la disponibilidad del dron y excluir aquellos que no estén habilitados para operar.

**Validación:** PR-01 debe comprobar que no se asignen drones no disponibles. PR-04 debe verificar la gestión de mantenimiento.

### 7.2. Alertas y fallos de servicios externos

**Conflicto:** la indisponibilidad de una dependencia puede impedir la consulta meteorológica o el envío de una alerta.

**Resolución:** el sistema debe gestionar los errores de manera controlada, evitar operaciones incorrectas y comunicar la imposibilidad de completar una acción cuando corresponda.

**Validación:** PR-11 debe comprobar el comportamiento frente a fallos de dependencias. Las pruebas de alertas deben verificar que los fallos detectados generen las notificaciones previstas.

### 7.3. Permisos y administración por sede

**Conflicto:** un coordinador podría intentar modificar la configuración de una sede distinta de la que tiene asignada o ejecutar funciones exclusivas de otro rol.

**Resolución:** los permisos deben validarse tanto por rol como por ámbito de sede.

**Validación:** PR-06 y PR-09 deben comprobar que se rechacen las operaciones no autorizadas.

## 8. Criterios de completitud de la matriz

La matriz se considera completa cuando:

1. Los ocho requisitos funcionales tienen código, nombre, prioridad MoSCoW, caso de uso, historia de usuario y prueba asociada.
2. Los cuatro requisitos no funcionales tienen un método de verificación definido.
3. Cada historia de usuario está vinculada a su requisito correspondiente mediante una clave real de Jira.
4. Los casos de uso están documentados y corresponden al comportamiento esperado del sistema.
5. Las pruebas referenciadas están implementadas o cuentan con un plan de prueba aprobado.
6. Las contradicciones detectadas tienen reglas de resolución y pruebas de aceptación.
7. Existe evidencia verificable de la ejecución de las pruebas.

Las referencias PR-01 a PR-12 son identificadores documentales propuestos. Deben asociarse con las pruebas reales del proyecto y no deben presentarse como pruebas ejecutadas si aún no se han realizado.

![Evidencia del Reto 6](docs/evidencias/Reto6.png)