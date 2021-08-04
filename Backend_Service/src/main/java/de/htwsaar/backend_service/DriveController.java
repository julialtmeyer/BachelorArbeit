package de.htwsaar.backend_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.Naviagtion.Coordinate;
import de.htwsaar.backend_service.Naviagtion.Node;
import de.htwsaar.backend_service.messages.Command;
import de.htwsaar.backend_service.messages.CommandList;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.RobotRepository;
import de.htwsaar.backend_service.messages.DriveCommand;
import de.htwsaar.backend_service.messages.TurnCommand;
import de.htwsaar.backend_service.mqtt.Publisher;
import de.htwsaar.backend_service.rest.HttpClient;
import org.apache.commons.math3.util.Precision;
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

        Coordinate startCoord = new Coordinate();
        Coordinate destCoord = new Coordinate();
        Coordinate directionCoord = new Coordinate();
        List<Command> commands = new ArrayList<>();
        for(int i = 0; i < path.size()-1; i++){
            if(i == 0){
                directionCoord.setX(robot.getRobot_info().getDirection_x());
                directionCoord.setY(robot.getRobot_info().getDirection_y());
            }
            Node start = path.get(i);
            Node dest = path.get(i+1);

            startCoord.setX(start.getX());
            startCoord.setY(start.getY());
            destCoord.setX(dest.getX());
            destCoord.setY(dest.getY());

            Double distance = getDistance(startCoord, destCoord);
            Double degrees =  getDirection(startCoord, destCoord, directionCoord, distance);

            if(degrees != 0.0){
                TurnCommand turn = new TurnCommand(degrees);
                commands.add(turn);
            }
            distance = getActualDistance(distance);
            DriveCommand drive = new DriveCommand(distance);
            commands.add(drive);

            directionCoord = getNextDirectionPoint(startCoord, destCoord);
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

    /**
     * calculates the angle the robot has to turn to get to the next point
     *
     * @param start start point
     * @param dest destination point
     * @param direction direction in which robot is facing
     * @param distance distance between points start and dest
     * @return angle the robot has to turn
     */
    private Double getDirection(Coordinate start, Coordinate dest, Coordinate direction, Double distance){
        Double directionDegrees;
        Double distanceStartDirection =  Precision.round(getDistance(start, direction), 2);
        Double distanceDirectionDest = Precision.round(getDistance(direction, dest), 2);

        if(isRightAngled(distanceStartDirection, distance, distanceDirectionDest)){
            directionDegrees = 90.0;
        }

        else if(isValidTriangle(distanceStartDirection, distance, distanceDirectionDest)){
            Double a = square(distance) + square(distanceStartDirection) - square(distanceDirectionDest);
            Double b = 2 * distance * distanceStartDirection;

            directionDegrees = Math.acos(a/b);
        }
        else
            directionDegrees = 0.0;

        double position = getRelativePosition(start, direction, dest);

        directionDegrees = directionDegrees * position;

        return directionDegrees;

    }

    private Double getActualDistance(Double distance){
        Double actual = distance * config.getMapScaleFactor();
        return actual;
    }

    /**
     * calculates new point that is on the same line as a and b with a set distance
     * @param a 1st point of line
     * @param b 2nd point of line
     * @return new point that is on same line as a and b
     */
    private Coordinate getNextDirectionPoint(Coordinate a, Coordinate b){
       Double distance = 200.0;
       Double aXBx = b.getX() -a.getX();
       Double aYBy = b.getY() - a.getY();
       Double mag = Math.sqrt(square(aXBx) + square(aYBy));
       Double point3x = b.getX() + distance * aXBx / mag;
       Double point3y = b.getY() + distance * aYBy / mag;

       Coordinate newDirection = new Coordinate(point3x, point3y);

       return newDirection;
    }

    /**
     *
     * @param a 1st point of line
     * @param b 2md Point of line
     * @param c point to locate
     * @return
     */
    private double getRelativePosition(Coordinate a, Coordinate b, Coordinate c){
        double position = Math.signum((b.getX() - a.getX()) * (c.getY() - a.getY()) - (b.getY()-a.getY()) * (c.getX()-a.getX()));

        return position;
    }

    private boolean isValidTriangle(Double a, Double b, Double c){
        if(a + b <= c || a + c <= b || b + c <= a){
            return false;
        }
        else
            return true;
    }

    private boolean isRightAngled(Double a, Double b, Double c){
        if(Math.abs(a*a + b*b - c*c) < 0.2){
            return true;
        }
        if(Math.abs(a*a + c*c - b*b) < 0.2){
            return true;
        }
        else
            return false;
    }

    /**
     *
     * @param a value to square
     * @return the squared value of a
     */
    private Double square(Double a){
        return a * a;
    }

    /**
     * calculates distance between two Points with Pythagorean theorem
     * @param a point A
     * @param b point B
     * @return distance between A and B
     */
    private Double getDistance(Coordinate a, Coordinate b){
        Double distance;
        Double x = Math.abs(a.getX() - b.getX());
        Double y = Math.abs(a.getY() - b.getY());

        if(x == 0.0 || y == 0.0){
            distance = x + y;
        }
        else {
            x = x * x;
            y = y * y;

            distance = Math.sqrt(x + y);
        }

        return distance;
    }

    private String toJSON(Object o) throws JsonProcessingException {
        String json;
        ObjectMapper mapper = new ObjectMapper();
        return json = mapper.writeValueAsString(o);
    }
}
