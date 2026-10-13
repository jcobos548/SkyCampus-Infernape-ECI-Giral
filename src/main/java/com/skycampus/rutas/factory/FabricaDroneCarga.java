package com.skycampus.rutas.factory;

import com.skycampus.rutas.drone.Drone;
import com.skycampus.rutas.drone.DroneCarga;

public class FabricaDroneCarga extends FabricaDrone {

    @Override
    public Drone crearDrone() {
        return new DroneCarga();
    }
}