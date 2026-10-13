package com.skycampus.domain;

import java.util.List;
import java.util.Optional;

public interface EstrategiaAsignacion {

    Optional<Drone> seleccionar(
            List<Drone> dronesDisponibles,
            boolean urgente,
            double pesoCargaKg
    );
}