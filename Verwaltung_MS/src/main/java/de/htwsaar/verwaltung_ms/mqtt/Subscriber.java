package de.htwsaar.verwaltung_ms.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.htwsaar.verwaltung_ms.Configuration;
import de.htwsaar.verwaltung_ms.Verwaltung;
import de.htwsaar.verwaltung_ms.mqtt.messages.Heartbeat;
import de.htwsaar.verwaltung_ms.mqtt.messages.Request;
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

    private Verwaltung verwaltung;

    /**
     * Instantiates a new Subscriber.
     */
    public Subscriber(Client client, Configuration config, Verwaltung verwaltung){
        this.client = client;
        this.config = config;
        this.verwaltung = verwaltung;
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
       /* String[] parts = payloadAsString.split("type:|;|,");
        String type = parts[1];
        String encoding = parts[2];
        String payload = parts[3];*/
        if(payloadAsString.toLowerCase().contains("request")){
            System.out.println("Registration Request received.");
            Request request = objectMapper.readValue(payloadAsString, Request.class);
            verwaltung.registerRobot(request);
        }
       else if(payloadAsString.toLowerCase().contains("heartbeat")){
            System.out.println("Heartbeat received");
            Heartbeat heartbeat = objectMapper.readValue(payloadAsString, Heartbeat.class);
            verwaltung.checkHeartbeat(heartbeat);

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
