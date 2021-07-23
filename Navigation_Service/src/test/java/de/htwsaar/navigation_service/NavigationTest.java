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
        Optional<Node> node = navigationController.findNodeinGraphWithCoordinates(x ,y);
        assert node.isPresent();
    }

    @Test
    public void testDistance(){

    }

    @Test
    public void findPathExactNodeCoordNoSkippingCorrelation(){
        Integer start_x = 596;
        Integer start_y = 577;
        Integer dest_x = 722;
        Integer dest_y = 509;


        navigationController.pathFromPointAtoPointB(start_x, start_y, dest_x, dest_y);
    }

    @Test
    public void findPathExactNodeCoordStreetToStreetSkipCorrelation(){
        Integer start_x = 596;
        Integer start_y = 444;
        Integer dest_x = 1007;
        Integer dest_y = 444;


        navigationController.pathFromPointAtoPointB(start_x, start_y, dest_x, dest_y);
    }
}
