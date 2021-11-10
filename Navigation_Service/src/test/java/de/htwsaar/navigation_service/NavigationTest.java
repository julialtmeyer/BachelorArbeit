package de.htwsaar.navigation_service;

import de.htwsaar.navigation_service.map.Node;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
public class NavigationTest {

    @Autowired
    private NavigationController navigationController;

    @Test
    public void testFindANode(){
        Integer x = 596;
        Integer y = 444;
    }

    @Test
    public void testDistance(){

    }

    @Test
    public void findPathExactNodeCoordStreetToStreetSkipCorrelation(){
        NavigationRequest navigationRequest = new NavigationRequest(596, 577, 1007, 444);


        navigationController.pathFromPointAtoPointB(navigationRequest);
    }
}
