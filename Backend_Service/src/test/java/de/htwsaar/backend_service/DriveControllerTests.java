package de.htwsaar.backend_service;

import de.htwsaar.backend_service.messages.Command;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.Robot_Info;
import org.apache.commons.math3.util.Precision;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.operation.overlayng.PrecisionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class DriveControllerTests {

    @Autowired
    private DriveController driveController;

    @Test
    public void testDriveAToBA1(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(596.0);
        info.setLocation_y(577.0);
        info.setDirection(190.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(1194.0);
        destination.setY(52.0);
        List<Command> result = driveController.driveFromAtoB(robot, destination);
        String expected = "[TurnCommand{direction=170.0}, DriveCommand{direction=26.41}, TurnCommand{direction=90.0}," +
                " DriveCommand{direction=81.61}, DriveCommand{direction=30.38}, TurnCommand{direction=-90.0}, " +
                "DriveCommand{direction=77.84}, TurnCommand{direction=90.0}, DriveCommand{direction=6.75}]";
        assert result.toString().equals(expected);
    }

    @Test
    public void testDriveAToBA2(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(1194.0);
        info.setLocation_y(52.0);
        info.setDirection(90.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(596.0);
        destination.setY(577.0);
        List<Command> result = driveController.driveFromAtoB(robot, destination);
        String expected = "[TurnCommand{direction=180.0}, DriveCommand{direction=6.75}, TurnCommand{direction=-90.0}, " +
                "DriveCommand{direction=77.84}, TurnCommand{direction=90.0}, DriveCommand{direction=30.38}, " +
                "DriveCommand{direction=81.61}, TurnCommand{direction=-90.0}, DriveCommand{direction=26.41}]";
        assert result.toString().equals(expected);
    }

    @Test
    public void testDriveAToBA3(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(596.0);
        info.setLocation_y(577.0);
        info.setDirection(90.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(151.0);
        destination.setY(136.0);
        List<Command> result = driveController.driveFromAtoB(robot, destination);
        String expected = "[TurnCommand{direction=-90.0}, DriveCommand{direction=26.41}, TurnCommand{direction=-90.0}, " +
                "DriveCommand{direction=88.36}, TurnCommand{direction=90.0}, DriveCommand{direction=61.16}]";
        assert result.toString().equals(expected);
    }

    @Test
    public void testDriveAToBA4(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(151.0);
        info.setLocation_y(136.0);
        info.setDirection(90.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(1160.0);
        destination.setY(444.0);
        List<Command> result = driveController.driveFromAtoB(robot, destination);
        String expected = "[TurnCommand{direction=90.0}, DriveCommand{direction=61.16}, " +
                "TurnCommand{direction=-90.0}, DriveCommand{direction=88.36}, " +
                "DriveCommand{direction=81.61}, DriveCommand{direction=30.38}]";
        assert result.toString().equals(expected);
    }

    @Test
    public void testDriveAToBA5(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(20.0);
        info.setLocation_y(444.0);
        info.setDirection(90.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(151.0);
        destination.setY(136.0);
        List<Command> result = driveController.driveFromAtoB(robot, destination);
        String expected = "[DriveCommand{direction=26.01}, TurnCommand{direction=-90.0}, DriveCommand{direction=61.16}]";
        assert result.toString().equals(expected);
    }

    @Test
    public void testDriveAToBA6(){
        Robot robot = new Robot();
        Robot_Info info = new Robot_Info();
        info.setLocation_x(151.0);
        info.setLocation_y(136.0);
        info.setDirection(90.0);
        robot.setRobot_info(info);
        Coordinate destination = new Coordinate();
        destination.setX(596.0);
        destination.setY(292.0);
        List<Command> result = driveController.driveFromAtoB(robot, destination);
        String expected = "[DriveCommand{direction=88.36}, TurnCommand{direction=90.0}, DriveCommand{direction=31.18}]";
        assert result.toString().equals(expected);
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
        Double angle = Math.toDegrees(Angle.angleBetween(tempCoord, startCoord, destCoord));
        double degrees =  robot.getRobot_info().getDirection() - angle;
        degrees = Precision.round(degrees, 2);
        assert degrees == 256.51;
    }
}
