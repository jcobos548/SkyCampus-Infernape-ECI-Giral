package com.skycampus.domain;

public interface ObservadorDrone {
    void notificar(String mensaje, Drone drone);
}
