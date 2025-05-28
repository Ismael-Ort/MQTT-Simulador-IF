package org.javadominicano.cmp;

import com.google.gson.Gson;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

public class Publicador {

    private static final String BROKER_URL = "tcp://mqtt.eict.ce.pucmm.edu.do:1883";
    private MqttClient client;
    private static DatabaseManager dbManager = new DatabaseManager();
    private static int[] stationIds = new int[4];
    private static int[] sensorIds = new int[4];

    public Publicador(String id) {
        try {
            client = new MqttClient(BROKER_URL, id);
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void enviarMensaje(String topic, String mensaje, Sensor sensor, int sensorId) {
        System.out.println("Enviando Información Topic: " + topic);
        try {
            MqttConnectOptions connectOptions = new MqttConnectOptions();
            connectOptions.setAutomaticReconnect(true);
            connectOptions.setCleanSession(false);
            connectOptions.setUserName("itt363-grupo3");
            connectOptions.setPassword("CnFebqnjbq7F".toCharArray());

            client.connect(connectOptions);
            client.publish(topic, mensaje.getBytes(), 2, false);
            client.disconnect();
            client.close();

            // Insertar en la base de datos
            dbManager.insertRecord(sensorId, sensor.getTemperatura(), sensor.getFecha());
        } catch (MqttException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void iniciarPrueba() {
        
        stationIds[0] = dbManager.insertStation("Modelo_E1", "Ubicación 1");
        stationIds[1] = dbManager.insertStation("Modelo_E2", "Ubicación 2");
        stationIds[2] = dbManager.insertStation("Modelo_E3", "Ubicación 3");
        stationIds[3] = dbManager.insertStation("Modelo_E4", "Ubicación 4");

        sensorIds[0] = dbManager.insertSensor(stationIds[0], "SensorA", "Temperatura");
        sensorIds[1] = dbManager.insertSensor(stationIds[1], "SensorB", "Temperatura");
        sensorIds[2] = dbManager.insertSensor(stationIds[2], "SensorC", "Temperatura");
        sensorIds[3] = dbManager.insertSensor(stationIds[3], "SensorD", "Temperatura");

        // Sensor A - Estación 1
        Thread hilo1 = new Thread(() -> {
            while (true) {
                Gson gson = new Gson();
                Sensor sensor = new Sensor("sensora", "Temperatura");
                new Publicador("sensora").enviarMensaje(
                        "/itt363-grupo3/estacion-1/sensores/datos",
                        gson.toJson(sensor), sensor, sensorIds[0]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        hilo1.start();

        // Sensor B - Estación 2
        Thread hilo2 = new Thread(() -> {
            while (true) {
                Gson gson = new Gson();
                Sensor sensor = new Sensor("sensorb", "Temperatura");
                new Publicador("sensorb").enviarMensaje(
                        "/itt363-grupo3/estacion-2/sensores/datos",
                        gson.toJson(sensor), sensor, sensorIds[1]);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        hilo2.start();

        // Sensor C - Estación 3
        Thread hilo3 = new Thread(() -> {
            while (true) {
                Gson gson = new Gson();
                Sensor sensor = new Sensor("sensorc", "Temperatura");
                new Publicador("sensorc").enviarMensaje(
                        "/itt363-grupo3/estacion-3/sensores/datos",
                        gson.toJson(sensor), sensor, sensorIds[2]);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        hilo3.start();

        // Sensor D - Estación 4
        Thread hilo4 = new Thread(() -> {
            while (true) {
                Gson gson = new Gson();
                Sensor sensor = new Sensor("sensord", "Temperatura");
                new Publicador("sensord").enviarMensaje(
                        "/itt363-grupo3/estacion-4/sensores/datos",
                        gson.toJson(sensor), sensor, sensorIds[3]);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        hilo4.start();
    }
}