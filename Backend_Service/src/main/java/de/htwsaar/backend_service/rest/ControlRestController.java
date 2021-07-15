package de.htwsaar.backend_service.rest;

import de.htwsaar.backend_service.Constants;
import de.htwsaar.backend_service.DriveController;
import de.htwsaar.backend_service.data.Robot;
import de.htwsaar.backend_service.data.RobotRepository;
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
    public ResponseEntity postDriveDistanceCommand(@RequestParam Integer distance, @PathVariable Long id){
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
    public ResponseEntity postTurnCommand(@RequestParam Integer distance, @PathVariable Long id){
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
}
