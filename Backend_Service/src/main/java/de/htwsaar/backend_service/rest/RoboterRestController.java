package de.htwsaar.backend_service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.Constants;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.RobotInfoRepository;
import de.htwsaar.backend_service.model.RobotRepository;
import de.htwsaar.backend_service.model.Robot_Info;
import org.locationtech.jts.geom.CoordinateXY;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/robots")
public class RoboterRestController {

    private RobotRepository robotRepository;
    private RobotInfoRepository robotInfoRepository;
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RoboterRestController.class);

    public RoboterRestController(RobotRepository robotRepository, RobotInfoRepository robotInfoRepository, ObjectMapper objectMapper) {
        this.robotRepository = robotRepository;
        this.robotInfoRepository = robotInfoRepository;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public ResponseEntity<String> getAllRobots() {
        String json = null;
        List<Robot> robots = robotRepository.findAll();
        try {
            json = objectMapper.writeValueAsString(robots);
        } catch (JsonProcessingException e) {
            logger.error("Failed to write objects to JSON! {}", robots, e);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<String> getAllActiveRobots() {
        String json = null;
        List<Robot> robots = robotRepository.findActiveRobots();
        try {
            json = objectMapper.writeValueAsString(robots);
        } catch (JsonProcessingException e) {
            logger.error("Failed to write objects to JSON! {}", robots, e);
        }
        return new ResponseEntity<>(json, HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity getRobotView(@PathVariable Long id){
        ResponseEntity response;
        Optional<Robot> robot = robotRepository.findById(id);
        if (robot.isPresent()){
        String json = "{\"robotID\": \"" + robot.get().getId() + "\",\"image\":\"" + robot.get().getRobot_info().getLast_picture() + "\"}";

        response = new ResponseEntity<>(json, HttpStatus.OK);
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROBOT_NOT_FOUND_EXCEPTION);

        return response;
    }

    @GetMapping("/{id}")
    public ResponseEntity getRobot(@PathVariable Long id){
        ResponseEntity response;
        Optional<Robot> robot = robotRepository.findById(id);
        if (robot.isPresent()){
            String json = null;
            try {
                json = objectMapper.writeValueAsString(robot);
            } catch (JsonProcessingException e) {
                logger.error("Failed to write objects to JSON! {}", robot, e);
            }

            response = new ResponseEntity<>(json, HttpStatus.OK);
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROBOT_NOT_FOUND_EXCEPTION);

        return response;
    }

    @PostMapping("/position/{id}")
    public ResponseEntity setRobotPosition(@RequestBody CoordinateXY position, @PathVariable Long id){
        ResponseEntity response;
        Optional<Robot> robotOptional = robotRepository.findById(id);
        if(robotOptional.isPresent()){
            Robot robot = robotOptional.get();
            robot.getRobot_info().setLocation_x(position.x);
            robot.getRobot_info().setLocation_y(position.y);
            Robot_Info robot_info = robot.getRobot_info();
            robot_info = robotInfoRepository.save(robot_info);
            robot.setRobot_info(robot_info);
            robot = robotRepository.save(robot);

            response = new ResponseEntity(HttpStatus.OK);

        }
        else {
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROBOT_NOT_FOUND_EXCEPTION);
        }

        return response;
    }
}
