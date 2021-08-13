package de.htwsaar.backend_service;

import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.Robot_Info;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.overlayng.PrecisionUtil;
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
        info.setDirection(180.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(1094.0);
        destination.setY(52.0);
        driveController.driveFromAtoB(robot, destination);
    }

    @Test
    public void testAngleCalculation(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(596.0);
        info.setLocation_y(577.0);
        info.setDirection(300.0);
        robot.setRobot_info(info);


        Coordinate startCoord = new Coordinate(robot.getRobot_info().getLocation_x(), robot.getRobot_info().getLocation_y());
        Coordinate destCoord = new Coordinate(1094.0, 52.0);
        Coordinate tempCoord = new Coordinate(startCoord.getX(), destCoord.getY());

        Double driveDistance = startCoord.distance(destCoord);

        Double angle = Math.toDegrees(Angle.angleBetween(tempCoord, startCoord, destCoord));
        double degrees =  robot.getRobot_info().getDirection() - angle;
        degrees = Precision.round(degrees, 2);
        assert degrees == 256.51;
    }
}
