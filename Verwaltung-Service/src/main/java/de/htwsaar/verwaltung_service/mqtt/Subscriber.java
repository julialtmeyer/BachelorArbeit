package de.htwsaar.verwaltung_service.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.verwaltung_service.Configuration;
import de.htwsaar.verwaltung_service.Verwaltung;
import de.htwsaar.verwaltung_service.mqtt.messages.Heartbeat;
import de.htwsaar.verwaltung_service.mqtt.messages.Request;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(Subscriber.class);

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
            logger.info("Subscribed to: {}", config.getTopic());
        } catch (MqttException me) {
            logger.error("Failed to subscribe to topic {}", config.getTopic(), me);
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        logger.warn("Connection lost!");
        try {
            mqttClient.connect();
        } catch (MqttException e) {
            logger.error("Cannot reconnect to broker!");
        }
    }

    /**
     *
     * @param topic the Topic from which the Message is from
     * @param message the Message recieved
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadAsString = message.toString();
        logger.info("Message received from {} on {}.",message.getId() ,topic);
        if(payloadAsString.toLowerCase().contains("request")){
            logger.info("Registration Request received.");
            Request request = null;
            try {
                request = objectMapper.readValue(payloadAsString, Request.class);
            } catch (JsonProcessingException e) {
                logger.error("Failed to read JSON {}", payloadAsString, e);
            }
            verwaltung.registerRobot(request);
        }
       else if(payloadAsString.toLowerCase().contains("heartbeat")){
            logger.info("Heartbeat received .");
            Heartbeat heartbeat = null;
            try {
                heartbeat = objectMapper.readValue(payloadAsString, Heartbeat.class);
            } catch (JsonProcessingException e) {
                logger.error("Failed to read JSON {}", payloadAsString, e);
            }
            verwaltung.checkHeartbeat(heartbeat);

        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }
}
