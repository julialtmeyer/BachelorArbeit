package de.htwsaar.backend_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.Naviagtion.Node;
import de.htwsaar.backend_service.messages.Command;
import de.htwsaar.backend_service.messages.CommandList;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.RobotRepository;
import de.htwsaar.backend_service.messages.DriveCommand;
import de.htwsaar.backend_service.messages.TurnCommand;
import de.htwsaar.backend_service.mqtt.Publisher;
import de.htwsaar.backend_service.rest.HttpClient;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriveController {

    private final Publisher publisher;

    private RobotRepository robotRepository;

    private HttpClient httpClient;

    private Configuration config;

    public DriveController(Publisher publisher, RobotRepository robotRepository, HttpClient httpClient, Configuration config) {
        this.publisher = publisher;
        this.robotRepository = robotRepository;
        this.httpClient = httpClient;
        this.config = config;
    }

    public void driveDistance(Robot robot, Double distance){
        DriveCommand driveCommand = new DriveCommand(distance);
        try {
            String driveJson = toJSON(driveCommand);
            String topic = config.getTopicRoot() + robot.getRoboterName() + config.getSubTopicDrive();
            publisher.publish(driveJson, topic, 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void turn(Robot robot, Double distance){
        TurnCommand turnCommand = new TurnCommand(distance);
        try {
            String turnJson = toJSON(turnCommand);
            String topic = config.getTopicRoot() + robot.getRoboterName() + config.getSubTopicDrive();
            publisher.publish(turnJson, topic, 0);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public boolean driveFromAtoB(Robot robot, Coordinate destination){
        List<Node> path;
        Coordinate robotLocation = new Coordinate(robot.getRobot_info().getLocation_x(), robot.getRobot_info().getLocation_y());
        path = httpClient.createNavigationPostRequest(robotLocation, destination);

        if(path.isEmpty()){
            return false;
        }


        List<Command> commands = new ArrayList<>();
        for(int i = 0; i < path.size()-1; i++){

            Node start = path.get(i);
            Node dest = path.get(i+1);

            Coordinate startCoord = new Coordinate(start.getX(), start.getY());
            Coordinate destCoord = new Coordinate(dest.getX(), dest.getY());
            Coordinate tempCoord = new Coordinate(startCoord.getX(), destCoord.getY());

            Double distance = startCoord.distance(destCoord);
            Double angle = Math.toDegrees(Angle.angleBetween(tempCoord, startCoord, destCoord));
            if (angle > 180){
                angle = 360-angle;
            }
            double degrees =  robot.getRobot_info().getDirection() - angle;

            if(degrees != 0.0){
                TurnCommand turn = new TurnCommand(degrees);
                commands.add(turn);
            }
            distance = getActualDistance(distance);
            DriveCommand drive = new DriveCommand(distance);
            commands.add(drive);
        }

        if(commands.isEmpty()){
            return false;
        }

        return publishCommands(commands, robot);
    }

    private boolean publishCommands(List<Command> commands, Robot robot){
        try {
            CommandList commandList = new CommandList(commands);
            String driveJson = toJSON(commandList);
            String topic = config.getTopicRoot() + robot.getRoboterName() + config.getSubTopicDrive();
            publisher.publish(driveJson, topic, 0);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Double getActualDistance(Double distance){
        Double actual = distance * config.getMapScaleFactor();
        return actual;
    }

    private String toJSON(Object o) throws JsonProcessingException {
        String json;
        ObjectMapper mapper = new ObjectMapper();
        return json = mapper.writeValueAsString(o);
    }
}
