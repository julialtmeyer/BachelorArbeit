package de.htwsaar.backend_service;

import de.htwsaar.backend_service.Naviagtion.Coordinate;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.Robot_Info;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DriveControllerTests {

    @Autowired
    private DriveController driveController;

    @Test
    public void testDriveAToB(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(596.0);
        info.setLocation_y(577.0);
        info.setDirection_x(596.0);
        info.setDirection_y(777.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(1094.0);
        destination.setY(52.0);
        driveController.driveFromAtoB(robot, destination);
    }
}
