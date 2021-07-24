package de.htwsaar.navigation_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.DataInput;
import java.io.IOException;

@RestController
@RequestMapping(path = "/navigation")
public class NavigationRestController {

    private NavigationController navigationController;

    private ObjectMapper objectMapper;

    public NavigationRestController(NavigationController navigationController, ObjectMapper objectMapper) {
        this.navigationController = navigationController;
        this.objectMapper = objectMapper;
    }


    @PostMapping("/")
    public ResponseEntity getNavigationFromAToB(@RequestBody NavigationRequest request){
        ResponseEntity response;
        try {
            String path = objectMapper.writeValueAsString(navigationController.pathFromPointAtoPointB(request));
            response = new ResponseEntity(path, HttpStatus.OK);

        } catch (JsonProcessingException e) {
            response = ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Request not in the right format");
            e.printStackTrace();
        }

        return response;
    }

}
