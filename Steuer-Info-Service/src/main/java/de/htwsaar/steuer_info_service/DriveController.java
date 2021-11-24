package de.htwsaar.steuer_info_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.steuer_info_service.Naviagtion.Node;
import de.htwsaar.steuer_info_service.messages.Command;
import de.htwsaar.steuer_info_service.messages.CommandList;
import de.htwsaar.steuer_info_service.model.Robot;
import de.htwsaar.steuer_info_service.model.RobotRepository;
import de.htwsaar.steuer_info_service.messages.DriveCommand;
import de.htwsaar.steuer_info_service.messages.TurnCommand;
import de.htwsaar.steuer_info_service.mqtt.Publisher;
import de.htwsaar.steuer_info_service.http.HttpClient;
import org.locationtech.jts.algorithm.Angle;
import org.locationtech.jts.geom.Coordinate;
import org.apache.commons.math3.util.Precision;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DriveController {

    private final Publisher publisher;

    private final RobotRepository robotRepository;

    private final HttpClient httpClient;

    private final Configuration config;

    private final Logger logger = LoggerFactory.getLogger(DriveController.class);


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
            logger.error("Failed to write JSON {}", driveCommand, e);
        }

    }

    public void turn(Robot robot, Double distance){
        TurnCommand turnCommand = new TurnCommand(distance);
        try {
            String turnJson = toJSON(turnCommand);
            String topic = config.getTopicRoot() + robot.getRoboterName() + config.getSubTopicDrive();
            publisher.publish(turnJson, topic, 0);
        } catch (JsonProcessingException e) {
            logger.error("Failed to write JSON {}", turnCommand, e);
        }

    }

    public List<Command> driveFromAtoB(Robot robot, Coordinate destination){
        List<Node> path;
        Coordinate robotLocation = new Coordinate(robot.getRobot_info().getLocation_x(), robot.getRobot_info().getLocation_y());
        path = httpClient.createNavigationPostRequest(robotLocation, destination);
        List<Command> commands = new ArrayList<>();

        if(path.isEmpty()){
            logger.error("No path found from {} to {}", robotLocation, destination);
            return commands;
        }

        //Get Direction of Robot
        double orientation = robot.getRobot_info().getDirection();

        for(int i = 0; i < path.size()-1; i++){

            Node start = path.get(i);
            Node dest = path.get(i+1);

            //turn Coordinate-Values in Coordinate-Object
            //and create a temporary Coordinate to build a triangle
            Coordinate startCoord = new Coordinate(start.getX(), start.getY());
            Coordinate destCoord = new Coordinate(dest.getX(), dest.getY());
            Coordinate tempCoord = new Coordinate(startCoord.getX(), destCoord.getY());

            double angle;

            double alpha = Math.toDegrees(Angle.angleBetween(tempCoord, startCoord, destCoord));
            if(start.getX() > dest.getX() && alpha == 180.0){
                alpha = 0.0;
            }

            if(start.getY() < dest.getY()){
                if(start.getX() < dest.getX()){
                    if (orientation > 180){
                        double a = 360 - orientation + 180 - alpha;
                        double b = (Math.abs(orientation - 180.0) + alpha) * -1;

                        angle = getSmaller(a, b);
                    }
                    else {
                        if(orientation > alpha){
                            angle = (Math.abs(orientation-180) - alpha) * -1;
                        }
                        else {
                            angle = alpha - Math.abs(orientation-180);
                        }
                    }
                }
                else {
                    if (orientation > 180){
                        if(orientation > alpha){
                            angle = (Math.abs(orientation-180) - alpha) * -1;
                        }
                        else {
                            angle = alpha - Math.abs(orientation-180);
                        }
                    }
                    else {
                        double a = 180 - orientation + alpha;
                        double b = (orientation + 180 - alpha) * -1;

                        angle = getSmaller(a,b);
                    }
                }

            }
            else {
                if(start.getY().equals(dest.getY()) && alpha == 0.0){
                    alpha = 90.0;
                }
                if(start.getX() < dest.getX()){
                    if(orientation > 180){
                        double a = (360-orientation) + alpha;
                        double b = (orientation - alpha) * -1;

                        angle = getSmaller(a,b);
                    }
                    else {
                        if(orientation > alpha){
                            angle = (orientation - alpha) * -1;
                        }
                        else {
                            angle = alpha - orientation;
                        }
                    }
                }
                else {
                    if(orientation > 180){
                        if((360 - orientation) > alpha){
                            angle = (360 - orientation) - alpha;
                        }
                        else {
                            angle = alpha - (360 - orientation);
                        }
                    }
                    else {
                        double a = (orientation + alpha) * -1;
                        double b = 360 - orientation - alpha;

                        angle = getSmaller(a,b);
                    }
                }
            }

            Double distance =  startCoord.distance(destCoord);

            if(angle != 0.0){
                TurnCommand turn = new TurnCommand(angle);
                commands.add(turn);
            }
            //remember latest orientation
            orientation = orientation + angle;

            if(orientation >= 360.0){
                orientation = orientation - 360;
            }
            else if( orientation < 0.0){
                orientation = 360 + orientation;
            }

            distance = getActualDistance(distance);
            DriveCommand drive = new DriveCommand(distance);
            commands.add(drive);
        }

        return commands;
    }

    public boolean handleAtoB(Robot robot, Coordinate destination){
        List<Command> commands = driveFromAtoB(robot, destination);
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
            logger.error("Failed to write JSON {}", commands, e);
            return false;
        }
    }

    private Double getActualDistance(Double distance){
        Double actual = distance * config.getMapScaleFactor();
        actual = Precision.round(actual, 2);
        return actual;
    }

    private String toJSON(Object o) throws JsonProcessingException {
        String json;
        ObjectMapper mapper = new ObjectMapper();
        return json = mapper.writeValueAsString(o);
    }

    private double getSmaller(double a, double b){
        if(Math.abs(a) < Math.abs(b))
            return a;
        else
            return b;
    }
}
