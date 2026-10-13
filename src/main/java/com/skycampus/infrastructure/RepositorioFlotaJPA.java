package com.skycampus.infrastructure;

import com.skycampus.domain.Drone;
import com.skycampus.domain.RepositorioFlota;

import java.util.List;

public class RepositorioFlotaJPA implements RepositorioFlota {

    @Override
    public List<Drone> findDisponibles(String sede) {
        throw new UnsupportedOperationException(
                "La integración con JPA todavía no está implementada"
        );
    }
}