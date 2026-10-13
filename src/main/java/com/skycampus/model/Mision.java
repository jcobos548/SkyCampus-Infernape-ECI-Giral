package com.skycampus.model;

import java.util.Objects;

public record Mision(
        Sede sede,
        EstadoMision estado,
        double tiempoEntregaMinutos,
        String droneId,
        boolean urgente
) {

    public Mision {
        Objects.requireNonNull(sede, "La sede no puede ser null");
        Objects.requireNonNull(estado, "El estado no puede ser null");
        Objects.requireNonNull(droneId, "El identificador del drone no puede ser null");

        if (droneId.isBlank()) {
            throw new IllegalArgumentException(
                    "El identificador del drone no puede estar vacío"
            );
        }

        if (!Double.isFinite(tiempoEntregaMinutos)
                || tiempoEntregaMinutos < 0) {
            throw new IllegalArgumentException(
                    "El tiempo de entrega debe ser un número válido y no negativo"
            );
        }
    }
}

