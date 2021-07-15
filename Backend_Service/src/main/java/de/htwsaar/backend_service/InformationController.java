package de.htwsaar.backend_service;

import de.htwsaar.backend_service.data.Robot;
import de.htwsaar.backend_service.data.Robot_Info;
import de.htwsaar.backend_service.messages.InformationMessage;
import de.htwsaar.backend_service.Configuration;
import de.htwsaar.backend_service.data.RobotInfoRepository;
import de.htwsaar.backend_service.data.RobotRepository;
import org.springframework.stereotype.Service;

@Service
public class InformationController {

    private RobotRepository robotRepository;
    private RobotInfoRepository robotInfoRepository;
    private final Configuration config;

    public InformationController(RobotRepository robotRepository, RobotInfoRepository robotInfoRepository, Configuration config) {
        this.robotRepository = robotRepository;
        this.robotInfoRepository = robotInfoRepository;
        this.config = config;
    }

    public void saveRobotInformation(String robotName, InformationMessage informationMessage){
        Robot robot = robotRepository.findRobotByName(robotName);
        Robot_Info robot_info = robot.getRobot_info();
        robot_info.setX_coord(informationMessage.getX_coord());
        robot_info.setY_coord(informationMessage.getY_coord());
        robot_info.setBattery(informationMessage.getBattery());
        robot_info.setLast_picture(informationMessage.getPicture());
        robotInfoRepository.save(robot_info);
    }
}
