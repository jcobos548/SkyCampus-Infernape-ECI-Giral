
package com.skycampus.rutas.drone;

public class DroneCarga implements Drone {

    @Override
    public void volar(
            String origen,
            String destino,
            double distanciaKm,
            double tiempoMinutos
    ) {
        System.out.printf(
                "Drone de carga: %s -> %s | Distancia: %.2f km | Tiempo: %.2f min%n",
                origen, destino, distanciaKm, tiempoMinutos
        );
    }
}
