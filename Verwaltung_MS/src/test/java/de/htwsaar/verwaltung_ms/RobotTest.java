package de.htwsaar.verwaltung_ms;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.verwaltung_ms.data.Robot;
import de.htwsaar.verwaltung_ms.data.Robot_Info;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RobotTest {

    @Test
    public void testRobotJson(){
        Robot robot = new Robot();
        robot.setId(1l);
        robot.setHsc("testHSC");
        robot.setMacAdr("2c:fh:ds");
        String robot_name = "Robot_1234";
        robot.setRoboterName(robot_name);
        Robot_Info robot_info = new Robot_Info();
        robot_info.setLocation_x(0);
        robot_info.setLocation_y(0);
        robot.setRobot_info(robot_info);

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            System.out.println(objectMapper.writeValueAsString(robot));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
