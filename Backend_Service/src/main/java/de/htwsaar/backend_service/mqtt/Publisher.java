package de.htwsaar.backend_service.mqtt;

import de.htwsaar.backend_service.Configuration;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

    private MqttClient mqttClient;

    private final Client client;

    private final Configuration config;

    public Publisher(Client client, Configuration config){
        this.client = client;
        this.config = config;
        config();
    }

    private void config(){
        this.mqttClient = client.client;
    }

    public void publish(String message, String topic,Integer qos) {
        MqttMessage mqttMessage = new MqttMessage(message.getBytes());
        mqttMessage.setQos(qos);
        if(mqttClient.isConnected()){
            try {
                mqttClient.publish(topic,mqttMessage);
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }
    }
}