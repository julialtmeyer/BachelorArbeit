package de.htwsaar.backend_service;

import de.htwsaar.backend_service.http.HttpClient;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.Robot_Info;
import de.htwsaar.backend_service.messages.InformationMessage;
import de.htwsaar.backend_service.model.RobotInfoRepository;
import de.htwsaar.backend_service.model.RobotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Service
public class InformationController {

    private final RobotRepository robotRepository;
    private final RobotInfoRepository robotInfoRepository;
    private final HttpClient httpClient;
    final Logger logger = LoggerFactory.getLogger(InformationController.class);

    public InformationController(RobotRepository robotRepository, RobotInfoRepository robotInfoRepository,HttpClient httpClient) {
        this.robotRepository = robotRepository;
        this.robotInfoRepository = robotInfoRepository;
        this.httpClient = httpClient;
    }

    public void saveRobotInformation(String robotName, InformationMessage informationMessage){
        try {
            Robot robot = robotRepository.findRobotByName(robotName);
            Robot_Info robot_info = robot.getRobot_info();
            robot_info.setLocation_x(informationMessage.getX_coord());
            robot_info.setLocation_y(informationMessage.getY_coord());
            robot_info.setBattery(informationMessage.getBattery());
            robot_info.setLast_picture(informationMessage.getPicture());
            robot_info.setDirection(informationMessage.getOrientation());
            robotInfoRepository.save(robot_info);
        }
        catch (IllegalArgumentException e){
            logger.error("Failed to save robot information {}{}", robotName, informationMessage, e);
        }
    }

    @Scheduled(fixedRate = 1000)
    private void updateActiveRobots(){
        List<Robot> activeRobotList;
        List<Robot> currentRobotList = robotRepository.findAll();
        try {
            activeRobotList = httpClient.createVerwaltungGetRequest();
            for (Robot activeRobot : activeRobotList){
                for (Robot robot: currentRobotList){
                    if(activeRobot.getId().equals(robot.getId())){
                        robot.setActive(activeRobot.isActive());
                        robot.setLastActive(activeRobot.getLastActive());
                    }
                }
            }
            //if there are robots that are not yet known to this service, they will be added
            if (activeRobotList.size() > currentRobotList.size()) {
                activeRobotList.removeAll(currentRobotList);

                for (Robot robot : activeRobotList) {
                    Robot_Info robot_info = robot.getRobot_info();
                    robot_info.setRobot(robot);
                    robot.setRobot_info(null);
                    robot = robotRepository.save(robot);
                    robot_info = robotInfoRepository.save(robot_info);
                    robot.setRobot_info(robot_info);
                    currentRobotList.add(robot);
                }
            }
            robotRepository.saveAll(currentRobotList);

        } catch (URISyntaxException e) {
            logger.error("Failed o build URI for HTTP-Request in methode updateActiveRobots() {}", e.getInput());
        }
    }
}
