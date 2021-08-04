package de.htwsaar.backend_service;

import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.Robot_Info;
import de.htwsaar.backend_service.messages.InformationMessage;
import de.htwsaar.backend_service.Configuration;
import de.htwsaar.backend_service.model.RobotInfoRepository;
import de.htwsaar.backend_service.model.RobotRepository;
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

    public void saveRobotInformation(String robotName, InformationMessage informationMessage) throws IllegalArgumentException{
        Robot robot = robotRepository.findRobotByName(robotName);
        Robot_Info robot_info = robot.getRobot_info();
        robot_info.setLocation_x(informationMessage.getX_coord());
        robot_info.setLocation_y(informationMessage.getY_coord());
        robot_info.setBattery(informationMessage.getBattery());
        robot_info.setLast_picture(informationMessage.getPicture());
        robotInfoRepository.save(robot_info);
    }
}
