package com.skycampus.dto;

import com.skycampus.model.Sede;

import java.util.Optional;

public record EstadisticasSede(
        Sede sede,
        long totalMisiones,
        long misionesEntregadas,
        double tasaExitoPorcentaje,
        double tiempoPromedioEntregaMinutos,
        Optional<String> droneMasUtilizado,
        double porcentajeMisionesUrgentes
) {
    public EstadisticasSede {
        droneMasUtilizado = droneMasUtilizado == null
                ? Optional.empty()
                : droneMasUtilizado;
    }
}
