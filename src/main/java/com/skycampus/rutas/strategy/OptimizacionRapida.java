package com.skycampus.rutas.strategy;

public class OptimizacionRapida implements EstrategiaOptimizacion {

    private static final double VELOCIDAD_KMH = 60.0;

    @Override
    public double calcularTiempoMinutos(double distanciaKm) {
        validarDistancia(distanciaKm);
        return distanciaKm / VELOCIDAD_KMH * 60.0;
    }

    private void validarDistancia(double distanciaKm) {
        if (!Double.isFinite(distanciaKm) || distanciaKm < 0) {
            throw new IllegalArgumentException(
                    "La distancia debe ser finita y no negativa"
            );
        }
    }
}
