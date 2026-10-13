package com.skycampus.domain;

import java.util.List;

public interface RepositorioFlota {
    List<Drone> findDisponibles(String sede);
}