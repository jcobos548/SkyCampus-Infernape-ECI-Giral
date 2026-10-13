package com.skycampus.infrastructure;

import com.skycampus.domain.ServicioClima;

public class ServicioClimaOpenWeather implements ServicioClima {

    @Override
    public boolean condicionesAptas(
            String origen,
            String destino
    ) {
        throw new UnsupportedOperationException(
                "La integración HTTP con OpenWeather todavía no está implementada"
        );
    }
}