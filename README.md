# SkyCampus-Infernape-ECI-Giral

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



