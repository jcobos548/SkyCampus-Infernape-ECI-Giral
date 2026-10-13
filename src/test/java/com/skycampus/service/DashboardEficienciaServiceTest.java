package com.skycampus.service;

import com.skycampus.dto.EstadisticasSede;
import com.skycampus.model.EstadoMision;
import com.skycampus.model.Mision;
import com.skycampus.model.Sede;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DashboardEficienciaServiceTest {

    private final DashboardEficienciaService servicio =
            new DashboardEficienciaService();

    static Stream<Arguments> escenarios() {
        Sede eci = new Sede("ECI");
        Sede unal = new Sede("UNAL");
        Sede uniandes = new Sede("Uniandes");
        Sede eafit = new Sede("EAFIT");

        return Stream.of(
                Arguments.of(
                        "Sede vacía",
                        List.of(eci),
                        List.<Mision>of(),
                        Map.of("ECI", new Esperado(0, 0, 0, 0, null))
                ),
                Arguments.of(
                        "Sede con una misión",
                        List.of(eci),
                        List.of(new Mision(
                                eci, EstadoMision.ENTREGADA,
                                20, "DR-01", true
                        )),
                        Map.of("ECI", new Esperado(
                                1, 100, 20, 100, "DR-01"
                        ))
                ),
                Arguments.of(
                        "Empate entre sedes",
                        List.of(eci, unal),
                        List.of(
                                new Mision(eci, EstadoMision.ENTREGADA,
                                        10, "DR-01", false),
                                new Mision(unal, EstadoMision.ENTREGADA,
                                        10, "DR-02", false)
                        ),
                        Map.of(
                                "ECI", new Esperado(1, 100, 10, 0, "DR-01"),
                                "UNAL", new Esperado(1, 100, 10, 0, "DR-02")
                        )
                ),
                Arguments.of(
                        "Red completa",
                        List.of(eci, unal, uniandes, eafit),
                        List.of(
                                new Mision(eci, EstadoMision.ENTREGADA,
                                        10, "DR-01", true),
                                new Mision(eci, EstadoMision.FALLIDA,
                                        0, "DR-02", false),
                                new Mision(unal, EstadoMision.ENTREGADA,
                                        30, "DR-03", false),
                                new Mision(uniandes, EstadoMision.ENTREGADA,
                                        40, "DR-04", true),
                                new Mision(eafit, EstadoMision.PENDIENTE,
                                        0, "DR-05", false)
                        ),
                        Map.of(
                                "ECI", new Esperado(2, 50, 10, 50, "DR-01"),
                                "UNAL", new Esperado(1, 100, 30, 0, "DR-03"),
                                "Uniandes", new Esperado(1, 100, 40, 100, "DR-04"),
                                "EAFIT", new Esperado(1, 0, 0, 0, "DR-05")
                        )
                )
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("escenarios")
    void calculaEstadisticasPorSede(
            String nombreEscenario,
            List<Sede> sedes,
            List<Mision> misiones,
            Map<String, Esperado> esperados
    ) {
        List<EstadisticasSede> resultados =
                servicio.calcularEstadisticas(sedes, misiones);

        assertEquals(sedes.size(), resultados.size());

        Map<String, EstadisticasSede> porNombre = resultados.stream()
                .collect(Collectors.toMap(
                        resultado -> resultado.sede().nombre(),
                        resultado -> resultado
                ));

        esperados.forEach((nombre, esperado) -> {
            EstadisticasSede resultado = porNombre.get(nombre);

            assertNotNull(resultado, "Falta la sede " + nombre);
            assertEquals(esperado.total(), resultado.totalMisiones());
            assertEquals(
                    esperado.tasaExito(),
                    resultado.tasaExitoPorcentaje(),
                    0.001
            );
            assertEquals(
                    esperado.tiempoPromedio(),
                    resultado.tiempoPromedioEntregaMinutos(),
                    0.001
            );
            assertEquals(
                    esperado.porcentajeUrgentes(),
                    resultado.porcentajeMisionesUrgentes(),
                    0.001
            );

            if (esperado.drone() == null) {
                assertTrue(resultado.droneMasUtilizado().isEmpty());
            } else {
                assertEquals(
                        esperado.drone(),
                        resultado.droneMasUtilizado().orElseThrow()
                );
            }
        });
    }

    private record Esperado(
            long total,
            double tasaExito,
            double tiempoPromedio,
            double porcentajeUrgentes,
            String drone
    ) {
    }
}
