
package com.skycampus.rutas.strategy;

public class OptimizacionEnergetica implements EstrategiaOptimizacion {

    private static final double VELOCIDAD_KMH = 40.0;

    @Override
    public double calcularTiempoMinutos(double distanciaKm) {
        if (!Double.isFinite(distanciaKm) || distanciaKm < 0) {
            throw new IllegalArgumentException(
                    "La distancia debe ser finita y no negativa"
            );
        }

        return distanciaKm / VELOCIDAD_KMH * 60.0;
    }
}