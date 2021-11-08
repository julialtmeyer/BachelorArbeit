package de.htwsaar.backend_service.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.Configuration;
import de.htwsaar.backend_service.InformationController;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.RobotRepository;
import de.htwsaar.backend_service.messages.InformationMessage;
import org.eclipse.paho.client.mqttv3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * The type Subscriber.
 */
@Component
public class Subscriber implements MqttCallback {

    private MqttClient mqttClient;

    private InformationController informationController;

    private final Client client;

    private final Configuration config;

    private RobotRepository robotRepository;

    private List<String> subscribedTopics;

    private final Logger logger = LoggerFactory.getLogger(Subscriber.class);

    /**
     * Instantiates a new Subscriber.
     */
    public Subscriber(InformationController informationController, Client client, Configuration config, RobotRepository robotRepository){
        this.informationController = informationController;
        this.client = client;
        this.config = config;
        this.robotRepository = robotRepository;
        config();
        this.subscribedTopics = new ArrayList<>();

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
    private void subscribe(String topic){
        try {
            this.mqttClient.subscribe(topic);
        } catch (MqttException me) {
            logger.error("Failed to subscribe to topic {}", topic, me);
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
        ObjectMapper objectMapper = new ObjectMapper();
        String payloadAsString = message.toString();
        if(payloadAsString.toLowerCase().contains("information")){
            String robotName = topicToRobotName(topic);
            logger.info("Message received from robot{} on topic {}", robotName, topic);
            InformationMessage info = null;
            try {
                info = objectMapper.readValue(payloadAsString, InformationMessage.class);
                informationController.saveRobotInformation(robotName, info);
            } catch (JsonProcessingException e) {
                logger.error("Failed to read JSON {}", payloadAsString, e);
            }
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

    }

    @Scheduled(fixedRate = 1000)
    private void subscribeToActiveRobots(){
        List<Robot> robotList = robotRepository.findActiveRobots();
        for(Robot robot: robotList){
            String topicInfo = String.format("%s%s%s",config.getTopicRoot(),robot.getRoboterName(),config.getSubTopicInformation());
            if(!subscribedTopics.contains(topicInfo)){
                subscribe(topicInfo);
                subscribedTopics.add(topicInfo);
                logger.info("Subscribed to: {}", topicInfo);
            }
        }
    }

    private String topicToRobotName(String topic){
        String split[] = topic.split("/");
        String robotName = split[2];
        return robotName;
    }
}
