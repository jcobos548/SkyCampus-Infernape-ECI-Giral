package com.skycampus.service;

import com.skycampus.dto.EstadisticasSede;
import com.skycampus.model.EstadoMision;
import com.skycampus.model.Mision;
import com.skycampus.model.Sede;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class DashboardEficienciaService {

    public List<EstadisticasSede> calcularEstadisticas(
            List<Sede> sedes,
            List<Mision> misiones
    ) {
        Objects.requireNonNull(sedes, "La lista de sedes no puede ser null");
        Objects.requireNonNull(misiones, "La lista de misiones no puede ser null");

        Map<Sede, Acumulador> acumulados = misiones.stream()
                .collect(Collectors.groupingBy(
                        Mision::sede,
                        Collector.of(
                                Acumulador::new,
                                Acumulador::agregar,
                                Acumulador::combinar
                        )
                ));

        return sedes.stream()
                .map(sede -> crearEstadisticas(
                        sede,
                        acumulados.getOrDefault(sede, new Acumulador())
                ))
                .toList();
    }

    private EstadisticasSede crearEstadisticas(
            Sede sede,
            Acumulador acumulador
    ) {
        long total = acumulador.totalMisiones;
        long entregadas = acumulador.misionesEntregadas;

        double tasaExito = porcentaje(entregadas, total);
        double tiempoPromedio = entregadas == 0
                ? 0.0
                : acumulador.tiempoTotalEntrega / entregadas;

        double porcentajeUrgentes = porcentaje(
                acumulador.misionesUrgentes,
                total
        );

        Optional<String> droneMasUtilizado = acumulador.usosPorDrone
                .entrySet()
                .stream()
                .max(
                        Comparator
                                .<Map.Entry<String, Long>>comparingLong(
                                        Map.Entry::getValue
                                )
                                .thenComparing(
                                        Map.Entry::getKey,
                                        Comparator.reverseOrder()
                                )
                )
                .map(Map.Entry::getKey);

        return new EstadisticasSede(
                sede,
                total,
                entregadas,
                tasaExito,
                tiempoPromedio,
                droneMasUtilizado,
                porcentajeUrgentes
        );
    }

    private double porcentaje(long cantidad, long total) {
        if (total == 0) {
            return 0.0;
        }

        return cantidad * 100.0 / total;
    }

    private static class Acumulador {

        private long totalMisiones;
        private long misionesEntregadas;
        private long misionesUrgentes;
        private double tiempoTotalEntrega;

        private final Map<String, Long> usosPorDrone = new HashMap<>();

        private void agregar(Mision mision) {
            totalMisiones++;

            if (mision.estado() == EstadoMision.ENTREGADA) {
                misionesEntregadas++;
                tiempoTotalEntrega += mision.tiempoEntregaMinutos();
            }

            if (mision.urgente()) {
                misionesUrgentes++;
            }

            usosPorDrone.merge(mision.droneId(), 1L, Long::sum);
        }

        private Acumulador combinar(Acumulador otro) {
            totalMisiones += otro.totalMisiones;
            misionesEntregadas += otro.misionesEntregadas;
            misionesUrgentes += otro.misionesUrgentes;
            tiempoTotalEntrega += otro.tiempoTotalEntrega;

            otro.usosPorDrone.forEach(
                    (drone, cantidad) ->
                            usosPorDrone.merge(drone, cantidad, Long::sum)
            );

            return this;
        }
    }
}
