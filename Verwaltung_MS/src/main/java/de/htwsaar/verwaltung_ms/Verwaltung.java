package de.htwsaar.verwaltung_ms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.verwaltung_ms.data.Robot;
import de.htwsaar.verwaltung_ms.data.RobotRepository;
import de.htwsaar.verwaltung_ms.mqtt.Publisher;
import de.htwsaar.verwaltung_ms.mqtt.messages.Heartbeat;
import de.htwsaar.verwaltung_ms.mqtt.messages.Request;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class Verwaltung {
    private RobotRepository robotRepository;
    private final Publisher publisher;
    private final Configuration config;

    public Verwaltung(RobotRepository robotRepository, Publisher publisher, Configuration config) {
        this.robotRepository = robotRepository;
        this.publisher = publisher;
        this.config = config;
    }

    public void registerRobot(Request request){
        Robot robot = robotRepository.findRobotByMacAdr(request.getMacAdr());
        if(robot == null){
            robot = new Robot();
            robot.setHsc(request.getHsc());
            robot.setMacAdr(request.getMacAdr());
            String robot_name = "Robot_" + rdmFourDigitNumber();
            robot.setRoboterName(robot_name);
            robot = robotRepository.save(robot);
        }

        else {
            robot.setHsc(request.getHsc());
            robot = robotRepository.save(robot);
        }

        String message = regMessageBuilder(robot);
        publisher.publish(message,config.getTopic(),0);
    }

    public void checkHeartbeat(Heartbeat heartbeat){
        Robot robot = robotRepository.findRobotByIdAndHscAndMacAdr(heartbeat.getId(),heartbeat.getHsc(),heartbeat.getMacAdr());
        if (robot == null){
            //To Do Error
        }

        else {
            robot.setActive(true);
            Instant instant = java.time.Instant.now();
            robot.setLastActive(instant);
            robotRepository.save(robot);
        }

    }

    private String regMessageBuilder(Robot robot){
        String message;
        String robotJson = "";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            robotJson = objectMapper.writeValueAsString(robot);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        message = String.format("{\"response\": %s}", robotJson);

        return message;
    }

    private String rdmFourDigitNumber(){
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }
}
