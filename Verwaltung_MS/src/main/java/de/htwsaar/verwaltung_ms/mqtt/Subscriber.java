package de.htwsaar.verwaltung_ms.mqtt;

import de.htwsaar.verwaltung_ms.Configuration;
import org.eclipse.paho.client.mqttv3.*;
import org.springframework.stereotype.Component;


/**
 * The type Subscriber.
 */
@Component
public class Subscriber implements MqttCallback {

    private MqttClient mqttClient;

    private final Client client;

    private final Configuration config;

    /**
     * Instantiates a new Subscriber.
     */
    public Subscriber(Client client, Configuration config){
        this.client = client;
        this.config = config;
        config();
        subscribe();
    }

    /**
     * Gets Client from Client and sets a Callback
     */
    private void config(){
        this.mqttClient = client.client;
        this.mqttClient.setCallback(this);
    }

    /**
     * Subrscribes to Topic
     */
    private void subscribe(){
        try {
            this.mqttClient.subscribe(config.getTopic());
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {

    }

    /**
     *
     * @param topic the Topic from which the Message is from
     * @param message the Message recieved
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        System.out.println("Message arrived");
        String payloadAsString = message.toString();
        String[] parts = payloadAsString.split("data:|;|,");
        String type = parts[1];
        String encoding = parts[2];
        String payload = parts[3];
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
