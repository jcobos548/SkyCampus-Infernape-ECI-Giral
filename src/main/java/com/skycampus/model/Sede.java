package com.skycampus.model;

import java.util.Objects;

public record Sede(String nombre) {

    public Sede {
        Objects.requireNonNull(nombre, "El nombre de la sede no puede ser null");

        if (nombre.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la sede no puede estar vacío"
            );
        }
    }
}
