package com.skycampus.rutas.factory;

import com.skycampus.rutas.drone.Drone;
import com.skycampus.rutas.drone.DroneRapido;

public class FabricaDroneRapido extends FabricaDrone {

    @Override
    public Drone crearDrone() {
        return new DroneRapido();
    }
}