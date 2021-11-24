package de.htwsaar.verwaltung_ms.mqtt;

import de.htwsaar.verwaltung_ms.Configuration;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

    private MqttClient mqttClient;

    private final Client client;

    private final Configuration config;

    private final Logger logger = LoggerFactory.getLogger(Publisher.class);

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
                logger.info("message published on {}", topic);
            } catch (MqttException e) {
                logger.error("error publishing message: {}",topic, e);
            }
        }
    }
}
