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
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class Verwaltung {

    private RobotRepository robotRepository;
    private RobotInfoRepository robotInfoRepository;
    private final Publisher publisher;
    private final Configuration config;

    public Verwaltung(RobotRepository robotRepository, RobotInfoRepository robotInfoRepository, Publisher publisher, Configuration config) {
        this.robotRepository = robotRepository;
        this.robotInfoRepository = robotInfoRepository;
        this.publisher = publisher;
        this.config = config;
    }

    public void registerRobot(Request request){
        if(!validMacAddress(request.getMacAdr())){
            System.out.println("mac fail");
        }
        Robot robot = robotRepository.findRobotByMacAdr(request.getMacAdr());
        if(robot == null){
            robot = new Robot();
            robot.setHsc(request.getHsc());
            robot.setMacAdr(request.getMacAdr());
            String robot_name = "Robot_" + rdmFourDigitNumber();
            robot.setRoboterName(robot_name);
            Robot_Info robot_info = new Robot_Info();
            robot_info.setX_coord(0);
            robot_info.setY_coord(0);
            robot_info.setRobot(robot);
            robot = robotRepository.save(robot);
            robot_info.setRobot(robot);
            robot_info = robotInfoRepository.save(robot_info);
            robot.setRobot_info(robot_info);
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
        if(!validMacAddress(heartbeat.getMacAdr())){
            System.out.println("mac fail");
        }
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
                String out = String.format("%s is now inactive!", robot.getRoboterName());
                System.out.println(out );
            }
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

    private boolean validMacAddress(String mac){
        boolean validMac;
        String macAdrPattern = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$";
        Pattern pattern = Pattern.compile(macAdrPattern);
        Matcher matcher = pattern.matcher(mac);
        return validMac = matcher.matches();
    }
}
