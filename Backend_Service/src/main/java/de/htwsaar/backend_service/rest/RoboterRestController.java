package de.htwsaar.backend_service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.RobotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/robots")
public class RoboterRestController {

    private RobotRepository robotRepository;
    private ObjectMapper objectMapper;


    public RoboterRestController(RobotRepository robotRepository, ObjectMapper objectMapper) {
        this.robotRepository = robotRepository;

        this.objectMapper = objectMapper;
    }

    @GetMapping("/")
    public ResponseEntity getAllActiveRobots() {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(robotRepository.findActiveRobots());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity(json, HttpStatus.OK);
    }

    @GetMapping("/{id}/image")
    public ResponseEntity getRobotView(@PathVariable Long id){
        Robot robot = robotRepository.getById(id);
        String json = "{\"robotID\": \"" + robot.getId() + "\",\"image\":\"" + robot.getRobot_info().getLast_picture() + "\"}";

        return new ResponseEntity(json, HttpStatus.OK);
    }
}
