package com.skycampus.rutas;

import com.skycampus.rutas.composite.Ruta;

import java.util.Objects;

public class GestorMisiones {

    public void ejecutarRuta(Ruta ruta) {
        Objects.requireNonNull(ruta, "La ruta no puede ser null");

        System.out.println("=== RESUMEN DE LA RUTA ===");
        System.out.printf(
                "Distancia total: %.2f km%n",
                ruta.calcularDistanciaKm()
        );
        System.out.println(
                "Cantidad de etapas: " + ruta.getCantidadEtapas()
        );

        System.out.println("=== EJECUCIÓN ===");
        ruta.ejecutar();

        System.out.println("=== RUTA FINALIZADA ===");
    }
}
