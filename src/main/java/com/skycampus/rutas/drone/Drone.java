package com.skycampus.rutas.drone;

public interface Drone {

    void volar(
            String origen,
            String destino,
            double distanciaKm,
            double tiempoMinutos
    );
}

