package de.htwsaar.backend_service.rest;

import de.htwsaar.backend_service.Constants;
import de.htwsaar.backend_service.DriveController;
import de.htwsaar.backend_service.Naviagtion.Coordinate;
import de.htwsaar.backend_service.model.Robot;
import de.htwsaar.backend_service.model.RobotRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping(path = "/control")
public class ControlRestController {

    private DriveController driveController;

    private RobotRepository robotRepository;

    public ControlRestController(DriveController driveController, RobotRepository robotRepository) {
        this.driveController = driveController;
        this.robotRepository = robotRepository;
    }

    @PostMapping("/drive/{id}")
    public ResponseEntity postDriveDistanceCommand(@RequestParam Double distance, @PathVariable Long id){
        ResponseEntity response;
        Optional<Robot> robot = robotRepository.findById(id);
        if (robot.isPresent()){
            driveController.driveDistance(robot.get(), distance);
            response = new ResponseEntity(HttpStatus.OK);
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROBOT_NOT_FOUND_EXCEPTION);

        return response;
    }

    @PostMapping("/turn/{id}")
    public ResponseEntity postTurnCommand(@RequestParam Double distance, @PathVariable Long id){
        ResponseEntity response;
        Optional<Robot> robot = robotRepository.findById(id);
        if (robot.isPresent()){
            if(robot.get().isActive()){
                driveController.turn(robot.get(), distance);
                response = new ResponseEntity(HttpStatus.OK);
            }
            else
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ROBOT_NOT_ACTIVE_EXCEPTION);
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROBOT_NOT_FOUND_EXCEPTION);

        return response;
    }

    @PostMapping("/AtoB/{id}")
    public ResponseEntity postDriveFromAToB(@RequestBody Coordinate destination, @PathVariable Long id){
        ResponseEntity response;
        Optional<Robot> robot = robotRepository.findById(id);
        if (robot.isPresent()){
            if(robot.get().isActive()){
                if(driveController.driveFromAtoB(robot.get(), destination)){
                    response = new ResponseEntity(HttpStatus.OK);
                }
                else {
                    response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ERROR_IN_NAVIGATION);
                }
            }
            else
                response = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Constants.ROBOT_NOT_ACTIVE_EXCEPTION);
        }
        else
            response = ResponseEntity.status(HttpStatus.NOT_FOUND).body(Constants.ROBOT_NOT_FOUND_EXCEPTION);

        return response;
    }
}
