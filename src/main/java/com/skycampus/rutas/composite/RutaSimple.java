package com.skycampus.rutas.composite;

import com.skycampus.rutas.drone.Drone;
import com.skycampus.rutas.factory.FabricaDrone;
import com.skycampus.rutas.observer.ObservadorEtapa;
import com.skycampus.rutas.strategy.EstrategiaOptimizacion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RutaSimple implements Ruta {

    private final String origen;
    private final String destino;
    private final double distanciaKm;
    private final EstrategiaOptimizacion estrategia;
    private final FabricaDrone fabricaDrone;
    private final List<ObservadorEtapa> observadores = new ArrayList<>();

    public RutaSimple(
            String origen,
            String destino,
            double distanciaKm,
            EstrategiaOptimizacion estrategia,
            FabricaDrone fabricaDrone
    ) {
        this.origen = validarLugar(origen, "El origen");
        this.destino = validarLugar(destino, "El destino");

        if (!Double.isFinite(distanciaKm) || distanciaKm <= 0) {
            throw new IllegalArgumentException(
                    "La distancia debe ser positiva y finita"
            );
        }

        this.distanciaKm = distanciaKm;
        this.estrategia = Objects.requireNonNull(
                estrategia, "La estrategia no puede ser null"
        );
        this.fabricaDrone = Objects.requireNonNull(
                fabricaDrone, "La fábrica de drones no puede ser null"
        );
    }

    private String validarLugar(String lugar, String campo) {
        Objects.requireNonNull(lugar, campo + " no puede ser null");

        if (lugar.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío"
            );
        }

        return lugar;
    }

    public void agregarObservador(ObservadorEtapa observador) {
        observadores.add(
                Objects.requireNonNull(
                        observador, "El observador no puede ser null"
                )
        );
    }

    public boolean eliminarObservador(ObservadorEtapa observador) {
        return observadores.remove(observador);
    }

    @Override
    public double calcularDistanciaKm() {
        return distanciaKm;
    }

    @Override
    public int getCantidadEtapas() {
        return 1;
    }

    @Override
    public void ejecutar() {
        double tiempoMinutos =
                estrategia.calcularTiempoMinutos(distanciaKm);

        Drone drone = fabricaDrone.crearDrone();

        notificar("Inicio de etapa: " + origen + " -> " + destino);

        try {
            drone.volar(
                    origen,
                    destino,
                    distanciaKm,
                    tiempoMinutos
            );

            notificar(
                    "Etapa completada: " + origen + " -> " + destino
            );
        } catch (RuntimeException excepcion) {
            notificar(
                    "Fallo en etapa: " + origen + " -> " + destino
            );
            throw excepcion;
        }
    }

    private void notificar(String mensaje) {
        for (ObservadorEtapa observador : observadores) {
            observador.actualizar(mensaje);
        }
    }
}