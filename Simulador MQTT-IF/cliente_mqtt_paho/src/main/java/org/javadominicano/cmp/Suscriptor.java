package org.javadominicano.cmp;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

/**
 * Representa el suscriptor
 */
public class Suscriptor {

    public static final String BROKER_URL = "tcp://mqtt.eict.ce.pucmm.edu.do:1883";
    private MqttClient client;

    public Suscriptor() {

        String clientId = "suscriptor-1";
        try {
            client = new MqttClient(BROKER_URL, clientId);
        }
        catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Inicio de la 
     */
    public void start() {

        try {
            client.setCallback(new SuscriptorCallback());
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setCleanSession(false);  // habilitando la opción de persistencia.
            connectOptions.setUserName("itt363-grupo3");
            connectOptions.setPassword("CnFebqnjbq7F".toCharArray());


            client.connect(connectOptions);
            client.subscribe("/itt363-grupo3/estacion-1/sensores/#"); // Recuperando la información desde la jerarquia superior.
            client.subscribe("/itt363-grupo3/estacion-2/sensores/#");
            client.subscribe("/itt363-grupo3/estacion-3/sensores/#");
            client.subscribe("/itt363-grupo3/estacion-4/sensores/#");

        }
        catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }

    }
}
