package de.htwsaar.verwaltung_service.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.verwaltung_service.data.Robot;
import de.htwsaar.verwaltung_service.data.RobotInfoRepository;
import de.htwsaar.verwaltung_service.data.RobotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/robots")
public class RobotRestController {

    private RobotRepository robotRepository;
    private RobotInfoRepository robotInfoRepository;
    private ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(RobotRestController.class);

    public RobotRestController(RobotRepository robotRepository, RobotInfoRepository robotInfoRepository, ObjectMapper objectMapper) {
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
}
