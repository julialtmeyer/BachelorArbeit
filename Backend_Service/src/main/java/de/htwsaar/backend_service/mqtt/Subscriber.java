package de.htwsaar.backend_service.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.Configuration;
import de.htwsaar.backend_service.data.Robot;
import de.htwsaar.backend_service.data.RobotRepository;
import de.htwsaar.backend_service.messages.Message;
import org.eclipse.paho.client.mqttv3.*;
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

    private final Client client;

    private final Configuration config;

    private RobotRepository robotRepository;

    private List<String> subscribedTopics;

    /**
     * Instantiates a new Subscriber.
     */
    public Subscriber(Client client, Configuration config, RobotRepository robotRepository){
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
        Message message1 = new Message(message.getPayload().toString(), topic, message.getQos());

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
                System.out.println("Subscribed to: " + topicInfo);
            }
        }
    }
}
