package de.htwsaar.verwaltung_ms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.verwaltung_ms.data.Robot;
import de.htwsaar.verwaltung_ms.data.RobotInfoRepository;
import de.htwsaar.verwaltung_ms.data.RobotRepository;
import de.htwsaar.verwaltung_ms.data.Robot_Info;
import de.htwsaar.verwaltung_ms.mqtt.Publisher;
import de.htwsaar.verwaltung_ms.mqtt.messages.Heartbeat;
import de.htwsaar.verwaltung_ms.mqtt.messages.Request;
import de.htwsaar.verwaltung_ms.rest.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Verwaltung {

    private final RobotRepository robotRepository;
    private final RobotInfoRepository robotInfoRepository;
    private final HttpClient httpClient;
    private final Publisher publisher;
    private final Configuration config;
    private final Logger logger = LoggerFactory.getLogger(Verwaltung.class);

    public Verwaltung(RobotRepository robotRepository,RobotInfoRepository robotInfoRepository,HttpClient httpClient, Publisher publisher, Configuration config) {
        this.robotRepository = robotRepository;
        this.robotInfoRepository = robotInfoRepository;
        this.publisher = publisher;
        this.config = config;
        this.httpClient = httpClient;
    }

    public void registerRobot(Request request){
        if(!validMacAddress(request.getMacAdr())){
            logger.error("Mac-address is invalid! {}", request.getMacAdr());
        }
        Optional<Robot> robotOptional = robotRepository.findRobotByMacAdr(request.getMacAdr());
        Robot robot;
        if(robotOptional.isEmpty()){
            logger.info("No Robot found with given MAC-Address, generating new Entry...");
            robot = new Robot();
            robot.setHsc(request.getHsc());
            robot.setMacAdr(request.getMacAdr());
            String robot_name = "Robot_" + rdmFourDigitNumber();
            robot.setRoboterName(robot_name);
            Robot_Info robot_info = new Robot_Info();
            robot_info.setLocation_x(config.getStandardStartLocationX());
            robot_info.setLocation_y(config.getStandardStartLocationY());
            robot_info.setDirection(0.0);
            robot_info.setRobot(robot);
            robot = robotRepository.save(robot);
            robot_info.setRobot(robot);
            robot_info = robotInfoRepository.save(robot_info);
            robot.setRobot_info(robot_info);
            robot = robotRepository.save(robot);
        }

        else {
            robotOptional.get().setHsc(request.getHsc());
            Robot robotTemp = httpClient.createPositionGetRequest(robotOptional.get().id);
            Robot_Info robot_info = robotOptional.get().getRobot_info();
            robot_info.setLocation_y(robotTemp.getRobot_info().getLocation_y());
            robot_info.setLocation_x(robotTemp.getRobot_info().getLocation_x());
            robot_info.setDirection(robotTemp.getRobot_info().getDirection());
            robot_info = robotInfoRepository.save(robot_info);
            robotOptional.get().setRobot_info(robot_info);
            robot = robotRepository.save(robotOptional.get());
        }

        String message = responseMessageBuilder(robot);
        publisher.publish(message,config.getTopic(),0);
        logger.info("Response to request from {} send.", robot.getRoboterName());
    }

    public void checkHeartbeat(Heartbeat heartbeat){
        if(!validMacAddress(heartbeat.getMacAdr())){
            logger.error("Mac-address is invalid! {}", heartbeat.getMacAdr());

        }
        Optional<Robot> robot = robotRepository.findRobotByIdAndHscAndMacAdr(heartbeat.getId(),heartbeat.getHsc(),heartbeat.getMacAdr());
        if (robot.isPresent()){
            robot.get().setActive(true);
            Instant instant = java.time.Instant.now();
            robot.get().setLastActive(instant);
            robotRepository.save(robot.get());

        }
        else {
            logger.error("Failed to find robot with id: {}, mac: {}, hsc: {}", heartbeat.getId(), heartbeat.getMacAdr(), heartbeat.getHsc());
        }

    }

    @Scheduled(fixedRate = 1000)
    public void checkActiveRobots(){
        List<Robot> robots = robotRepository.findActiveRobots();
        for(Robot robot: robots){
            Instant instantNow = Instant.now();
            Instant instantRobot = robot.getLastActive();
            Duration duration = Duration.between(instantRobot, instantNow);
            if(duration.getSeconds() > 10){
                robot.setActive(false);
                robotRepository.save(robot);
                logger.warn("{} is now inactive!", robot.getRoboterName());
            }
        }
    }

    private String responseMessageBuilder(Robot robot){
        String message;
        String robotJson = "";

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            robotJson = objectMapper.writeValueAsString(robot);

        } catch (JsonProcessingException e) {
           logger.error("Failed to write JSON {}", robot, e);
        }

        message = String.format("{\"response\": %s}", robotJson);

        return message;
    }

    private String rdmFourDigitNumber(){
        Random rnd = new Random();
        int number = rnd.nextInt(9999);
        return String.format("%04d", number);
    }

    private boolean validMacAddress(String mac){
        boolean validMac;
        String macAdrPattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        Pattern pattern = Pattern.compile(macAdrPattern);
        Matcher matcher = pattern.matcher(mac);
        return validMac = matcher.matches();
    }
}
