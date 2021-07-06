package de.htwsaar.backend_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.data.Robot;
import de.htwsaar.backend_service.data.RobotRepository;
import de.htwsaar.backend_service.messages.DriveCommand;
import de.htwsaar.backend_service.messages.TurnCommand;
import de.htwsaar.backend_service.mqtt.Publisher;
import org.springframework.stereotype.Service;

@Service
public class DriveController {

    private final Publisher publisher;

    private RobotRepository robotRepository;

    private Configuration config;

    public DriveController(Publisher publisher, RobotRepository robotRepository, Configuration config) {
        this.publisher = publisher;
        this.robotRepository = robotRepository;
        this.config = config;
    }

    public void driveDistance(Robot robot, int distance){
        DriveCommand driveCommand = new DriveCommand(distance);
        try {
            String driveJson = toJSON(driveCommand);
            String topic = config.getTopicRoot() + robot.getRoboterName() + config.getSubTopicDrive();
            publisher.publish(driveJson, topic, 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void turn(Robot robot, int distance){
        TurnCommand turnCommand = new TurnCommand(distance);
        try {
            String turnJson = toJSON(turnCommand);
            String topic = config.getTopicRoot() + robot.getRoboterName() + config.getSubTopicDrive();
            publisher.publish(turnJson, topic, 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void driveFromAtoB(Robot robot){

    }

    private String toJSON(Object o) throws JsonProcessingException {
        String json;
        ObjectMapper mapper = new ObjectMapper();
        return json = mapper.writeValueAsString(o);
    }
}
