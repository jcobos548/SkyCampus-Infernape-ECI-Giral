package com.skycampus.rutas;

import com.skycampus.rutas.composite.RutaCompuesta;
import com.skycampus.rutas.composite.RutaSimple;
import com.skycampus.rutas.factory.FabricaDroneCarga;
import com.skycampus.rutas.factory.FabricaDroneRapido;
import com.skycampus.rutas.observer.AlertaEtapa;
import com.skycampus.rutas.observer.ObservadorEtapa;
import com.skycampus.rutas.strategy.OptimizacionEnergetica;
import com.skycampus.rutas.strategy.OptimizacionRapida;

public class DemoRutas {

    public static void main(String[] args) {
        ObservadorEtapa alertas = new AlertaEtapa();

        RutaSimple primeraEtapa = new RutaSimple(
                "ECI",
                "Estación de carga",
                5.0,
                new OptimizacionEnergetica(),
                new FabricaDroneCarga()
        );

        primeraEtapa.agregarObservador(alertas);

        RutaSimple segundaEtapa = new RutaSimple(
                "Estación de carga",
                "UNAL",
                8.0,
                new OptimizacionRapida(),
                new FabricaDroneRapido()
        );

        segundaEtapa.agregarObservador(alertas);

        RutaCompuesta rutaCompleta = new RutaCompuesta();
        rutaCompleta.agregarEtapa(primeraEtapa);
        rutaCompleta.agregarEtapa(segundaEtapa);

        GestorMisiones gestor = new GestorMisiones();
        gestor.ejecutarRuta(rutaCompleta);
    }
}
