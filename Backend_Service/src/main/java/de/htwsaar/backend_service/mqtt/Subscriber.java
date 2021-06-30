package de.htwsaar.backend_service.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.Configuration;
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
    public void messageArrived(String topic, MqttMessage message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadAsString = message.toString();

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
