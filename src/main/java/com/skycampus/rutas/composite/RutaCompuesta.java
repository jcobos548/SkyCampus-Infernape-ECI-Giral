
package com.skycampus.rutas.composite;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RutaCompuesta implements Ruta {

    private final List<Ruta> etapas = new ArrayList<>();

    public void agregarEtapa(Ruta etapa) {
        etapas.add(Objects.requireNonNull(etapa));
    }

    @Override
    public double calcularDistanciaKm() {
        return etapas.stream()
                .mapToDouble(Ruta::calcularDistanciaKm)
                .sum();
    }

    @Override
    public int getCantidadEtapas() {
        return etapas.stream()
                .mapToInt(Ruta::getCantidadEtapas)
                .sum();
    }

    @Override
    public void ejecutar() {
        for (Ruta etapa : etapas) {
            etapa.ejecutar();
        }
    }
}
