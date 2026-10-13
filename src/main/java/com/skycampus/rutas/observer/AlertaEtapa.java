
package com.skycampus.rutas.observer;

public class AlertaEtapa implements ObservadorEtapa {

    @Override
    public void actualizar(String mensaje) {
        System.out.println("[ALERTA DE MISIÓN] " + mensaje);
    }
}
