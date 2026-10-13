package com.skycampus.domain;

import java.util.Objects;

public record Drone(
        String id,
        String sede,
        double bateriaPorcentaje,
        double capacidadKg,
        boolean disponible,
        TipoDrone tipo
) {
    public Drone {
        Objects.requireNonNull(id, "El id no puede ser null");
        Objects.requireNonNull(sede, "La sede no puede ser null");
        Objects.requireNonNull(tipo, "El tipo no puede ser null");

        if (id.isBlank() || sede.isBlank()) {
            throw new IllegalArgumentException(
                    "El id y la sede son obligatorios"
            );
        }

        if (!Double.isFinite(bateriaPorcentaje)
                || bateriaPorcentaje < 0
                || bateriaPorcentaje > 100) {
            throw new IllegalArgumentException(
                    "La batería debe estar entre 0 y 100"
            );
        }

        if (!Double.isFinite(capacidadKg) || capacidadKg < 0) {
            throw new IllegalArgumentException(
                    "La capacidad debe ser finita y no negativa"
            );
        }
    }
}
