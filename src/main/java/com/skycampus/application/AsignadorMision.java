package com.skycampus.application;

import com.skycampus.domain.Drone;
import com.skycampus.domain.EstrategiaAsignacion;
import com.skycampus.domain.ObservadorDrone;
import com.skycampus.domain.RepositorioFlota;
import com.skycampus.domain.ServicioClima;

import java.util.List;
import java.util.Objects;

public class AsignadorMision {

    private static final double BATERIA_MINIMA = 30.0;

    private final RepositorioFlota repo;
    private final ServicioClima clima;
    private final EstrategiaAsignacion estrategia;
    private final ObservadorDrone notificador;

    public AsignadorMision(
            RepositorioFlota repo,
            ServicioClima clima,
            EstrategiaAsignacion estrategia,
            ObservadorDrone notificador
    ) {
        this.repo = Objects.requireNonNull(repo);
        this.clima = Objects.requireNonNull(clima);
        this.estrategia = Objects.requireNonNull(estrategia);
        this.notificador = Objects.requireNonNull(notificador);
    }

    public Drone asignar(
            String sede,
            String origen,
            String destino,
            boolean urgente,
            double pesoCargaKg
    ) {
        validarTexto(sede, "La sede");
        validarTexto(origen, "El origen");
        validarTexto(destino, "El destino");

        if (!Double.isFinite(pesoCargaKg) || pesoCargaKg < 0) {
            throw new IllegalArgumentException(
                    "El peso debe ser finito y no negativo"
            );
        }

        if (!clima.condicionesAptas(origen, destino)) {
            notificador.notificar(
                    "Misión rechazada: condiciones climáticas no aptas",
                    null
            );

            throw new IllegalStateException(
                    "No se puede asignar la misión por condiciones climáticas"
            );
        }

        List<Drone> disponibles = repo.findDisponibles(sede);

        if (disponibles == null) {
            throw new IllegalStateException(
                    "El repositorio devolvió una lista null"
            );
        }

        List<Drone> aptos = disponibles.stream()
                .filter(Objects::nonNull)
                .filter(Drone::disponible)
                .filter(drone ->
                        drone.bateriaPorcentaje() >= BATERIA_MINIMA)
                .filter(drone ->
                        drone.capacidadKg() >= pesoCargaKg)
                .toList();

        Drone seleccionado = estrategia.seleccionar(
                        aptos,
                        urgente,
                        pesoCargaKg
                )
                .filter(aptos::contains)
                .orElseThrow(() -> new IllegalStateException(
                        "No hay drones disponibles que cumplan los requisitos"
                ));

        notificador.notificar(
                "Drone asignado a la misión",
                seleccionado
        );

        return seleccionado;
    }

    private void validarTexto(String valor, String campo) {
        Objects.requireNonNull(valor, campo + " no puede ser null");

        if (valor.isBlank()) {
            throw new IllegalArgumentException(
                    campo + " no puede estar vacío"
            );
        }
    }
}
