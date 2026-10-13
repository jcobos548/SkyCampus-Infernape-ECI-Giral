package com.skycampus.application;

import com.skycampus.domain.Drone;
import com.skycampus.domain.EstrategiaAsignacion;
import com.skycampus.domain.ObservadorDrone;
import com.skycampus.domain.RepositorioFlota;
import com.skycampus.domain.ServicioClima;
import com.skycampus.domain.TipoDrone;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsignadorMisionTest {

    @Mock
    private RepositorioFlota repo;

    @Mock
    private ServicioClima clima;

    @Mock
    private EstrategiaAsignacion estrategia;

    @Mock
    private ObservadorDrone notificador;

    private AsignadorMision asignador;
    private Drone droneApto;

    @BeforeEach
    void setUp() {
        asignador = new AsignadorMision(
                repo,
                clima,
                estrategia,
                notificador
        );

        droneApto = new Drone(
                "DR-01",
                "ECI",
                85.0,
                5.0,
                true,
                TipoDrone.CARGA
        );
    }

    @Test
    void asignaDroneCuandoClimaYFlotaSonAptos() {
        when(clima.condicionesAptas("ECI", "UNAL"))
                .thenReturn(true);

        when(repo.findDisponibles("ECI"))
                .thenReturn(List.of(droneApto));

        when(estrategia.seleccionar(
                List.of(droneApto), true, 3.0
        )).thenReturn(Optional.of(droneApto));

        Drone resultado = asignador.asignar(
                "ECI", "ECI", "UNAL", true, 3.0
        );

        assertEquals(droneApto, resultado);

        verify(clima).condicionesAptas("ECI", "UNAL");
        verify(repo).findDisponibles("ECI");
        verify(estrategia).seleccionar(
                List.of(droneApto), true, 3.0
        );
        verify(notificador).notificar(
                "Drone asignado a la misión", droneApto
        );
    }

    @Test
    void rechazaMisionSiElClimaNoEsApto() {
        when(clima.condicionesAptas("ECI", "UNAL"))
                .thenReturn(false);

        assertThrows(
                IllegalStateException.class,
                () -> asignador.asignar(
                        "ECI", "ECI", "UNAL", false, 1.0
                )
        );

        verify(repo, never()).findDisponibles(anyString());
        verifyNoInteractions(estrategia);
        verify(notificador).notificar(
                "Misión rechazada: condiciones climáticas no aptas",
                null
        );
    }

    @Test
    void rechazaMisionSiNoHayDronesDisponibles() {
        when(clima.condicionesAptas("ECI", "UNAL"))
                .thenReturn(true);

        when(repo.findDisponibles("ECI"))
                .thenReturn(List.of());

        when(estrategia.seleccionar(
                List.of(), false, 1.0
        )).thenReturn(Optional.empty());

        assertThrows(
                IllegalStateException.class,
                () -> asignador.asignar(
                        "ECI", "ECI", "UNAL", false, 1.0
                )
        );

        verify(notificador, never()).notificar(
                eq("Drone asignado a la misión"),
                any()
        );
    }

    @Test
    void descartaDronesConBateriaMenorAlTreintaPorCiento() {
        Drone bateriaBaja = new Drone(
                "DR-02", "ECI", 20.0, 10.0, true, TipoDrone.CARGA
        );

        when(clima.condicionesAptas("ECI", "UNAL"))
                .thenReturn(true);

        when(repo.findDisponibles("ECI"))
                .thenReturn(List.of(bateriaBaja));

        when(estrategia.seleccionar(
                List.of(), false, 1.0
        )).thenReturn(Optional.empty());

        assertThrows(
                IllegalStateException.class,
                () -> asignador.asignar(
                        "ECI", "ECI", "UNAL", false, 1.0
                )
        );

        verify(estrategia).seleccionar(List.of(), false, 1.0);
    }

    @Test
    void descartaDronesConCapacidadInsuficiente() {
        Drone capacidadInsuficiente = new Drone(
                "DR-03", "ECI", 90.0, 1.0, true, TipoDrone.CARGA
        );

        when(clima.condicionesAptas("ECI", "UNAL"))
                .thenReturn(true);

        when(repo.findDisponibles("ECI"))
                .thenReturn(List.of(capacidadInsuficiente));

        when(estrategia.seleccionar(
                List.of(), false, 2.0
        )).thenReturn(Optional.empty());

        assertThrows(
                IllegalStateException.class,
                () -> asignador.asignar(
                        "ECI", "ECI", "UNAL", false, 2.0
                )
        );

        verify(estrategia).seleccionar(List.of(), false, 2.0);
    }

    @Test
    void rechazaPesoNegativoSinConsultarDependencias() {
        assertThrows(
                IllegalArgumentException.class,
                () -> asignador.asignar(
                        "ECI", "ECI", "UNAL", false, -1.0
                )
        );

        verifyNoInteractions(repo, clima, estrategia, notificador);
    }

    @Test
    void rechazaDroneDevueltoPorEstrategiaQueNoEsCandidatoApto() {
        Drone otroDrone = new Drone(
                "DR-X", "ECI", 95.0, 10.0, true, TipoDrone.RAPIDO
        );

        when(clima.condicionesAptas("ECI", "UNAL"))
                .thenReturn(true);

        when(repo.findDisponibles("ECI"))
                .thenReturn(List.of(droneApto));

        when(estrategia.seleccionar(
                List.of(droneApto), false, 1.0
        )).thenReturn(Optional.of(otroDrone));

        assertThrows(
                IllegalStateException.class,
                () -> asignador.asignar(
                        "ECI", "ECI", "UNAL", false, 1.0
                )
        );

        verify(notificador, never()).notificar(
                eq("Drone asignado a la misión"),
                any()
        );
    }
}