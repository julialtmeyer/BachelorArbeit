package de.htwsaar.navigation_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.navigation_service.map.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

@SpringBootTest
public class MapTest {

    @Test
    public void testGraphToJSON(){
        MapGraph graph = new MapGraph();
        graph.setName("htw");
        graph.setId(0);
        ArrayList<Node> list = new ArrayList();

        StreetNode streetNode = new StreetNode(0, 0, 0, 1);
        StreetNode streetNode1 = new StreetNode(1, 2,2, 1);
        DestinationNode destinationNode = new DestinationNode(5,5,5,3);
        CorrelationNode correlationNode = new CorrelationNode(2, 5,6,2,destinationNode, streetNode, streetNode1);
        destinationNode.setEntryNode(correlationNode);

        streetNode.getNeighbors().add(streetNode1);
        streetNode.getNeighbors().add(correlationNode);
        streetNode1.getNeighbors().add(streetNode);
        streetNode1.getNeighbors().add(correlationNode);

        list.add(streetNode);
        list.add(streetNode1);
        list.add(correlationNode);
        list.add(destinationNode);
        graph.setNodes(list);

        ObjectMapper mapper = new ObjectMapper();
        try {
            String control = "{\"name\":\"htw\",\"id\":0,\"nodes\":[{\"StreetNode\":{\"id\":0,\"x\":0.0,\"y\":0.0,\"weight\":1,\"neighbors\":[{\"StreetNode\":{\"id\":1,\"x\":2.0,\"y\":2.0,\"weight\":1}},{\"CorrelationNode\":{\"id\":2,\"x\":5.0,\"y\":6.0,\"weight\":2}}]}},{\"StreetNode\":{\"id\":1,\"x\":2.0,\"y\":2.0,\"weight\":1,\"neighbors\":[{\"StreetNode\":{\"id\":0,\"x\":0.0,\"y\":0.0,\"weight\":1}},{\"CorrelationNode\":{\"id\":2,\"x\":5.0,\"y\":6.0,\"weight\":2}}]}},{\"CorrelationNode\":{\"id\":2,\"x\":5.0,\"y\":6.0,\"weight\":2,\"previousNode\":{\"StreetNode\":{\"id\":0,\"x\":0.0,\"y\":0.0,\"weight\":1}},\"nextNode\":{\"StreetNode\":{\"id\":1,\"x\":2.0,\"y\":2.0,\"weight\":1}},\"correlation\":{\"DestinationNode\":{\"id\":5,\"x\":5.0,\"y\":5.0,\"weight\":3}}}},{\"DestinationNode\":{\"id\":5,\"x\":5.0,\"y\":5.0,\"weight\":3,\"entryNode\":{\"CorrelationNode\":{\"id\":2,\"x\":5.0,\"y\":6.0,\"weight\":2}}}}]}";
            String map = mapper.writeValueAsString(graph);
            assert(map.equals(control));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
