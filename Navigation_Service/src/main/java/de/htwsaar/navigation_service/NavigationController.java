package de.htwsaar.navigation_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.navigation_service.map.MapManager;
import de.htwsaar.navigation_service.map.Node;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.alg.shortestpath.KShortestSimplePaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NavigationController {

    private final MapManager manager;
    private final Configuration configuration;

    public NavigationController(MapManager manager, Configuration configuration) {
        this.manager = manager;
        this.configuration = configuration;
    }

    public List<Node> pathFromPointAtoPointB(NavigationRequest navigationRequest){

        Optional<Node> startNode = findNodeinGraphWithCoordinates(navigationRequest.getStart_x(), navigationRequest.getStart_y());
        if(!startNode.isPresent()){
            System.out.println("no Node at start coordinates");
        }
        Optional<Node> destNode = findNodeinGraphWithCoordinates(navigationRequest.getDest_x(), navigationRequest.getDest_y());
        if(!destNode.isPresent()){
            System.out.println("no Node at destination coordinates");
        }

        return  dijkstraSearch(startNode.get(), destNode.get());

    }

    private List<Node> dijkstraSearch(Node start, Node dest){
        DirectedWeightedMultigraph<Node, DefaultWeightedEdge> map = manager.getGraph();
        GraphPath<Node, DefaultWeightedEdge> resultPath = BidirectionalDijkstraShortestPath.findPathBetween(map, start, dest);

        return  resultPath.getVertexList();
    }

    protected Optional<Node> findNodeinGraphWithCoordinates(Integer x, Integer y){

        Optional<Node> node = manager.getGraph().vertexSet().stream()
                .filter(n -> ((x <= n.getX()+configuration.getNodeToleranz()
                                && x >= n.getX()-configuration.getNodeToleranz())
                        && (y <= n.getY()+configuration.getNodeToleranz()
                                && y >= n.getY()-configuration.getNodeToleranz()))).findFirst();

        return node;
    }
}


