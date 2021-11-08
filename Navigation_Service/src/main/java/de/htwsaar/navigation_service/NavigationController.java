package de.htwsaar.navigation_service;

import de.htwsaar.navigation_service.map.MapManager;
import de.htwsaar.navigation_service.map.Node;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.BidirectionalDijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NavigationController {

    private final MapManager manager;
    private final Configuration configuration;

    private final Logger logger = LoggerFactory.getLogger(NavigationController.class);

    public NavigationController(MapManager manager, Configuration configuration) {
        this.manager = manager;
        this.configuration = configuration;
    }

    public List<Node> pathFromPointAtoPointB(NavigationRequest navigationRequest){

        Optional<Node> startNode = findNodeInGraphWithCoordinates(navigationRequest.getStart_x(), navigationRequest.getStart_y());
        if(startNode.isEmpty()){
            logger.error("No Node at start coordinates! {}", startNode);
        }
        Optional<Node> destNode = findNodeInGraphWithCoordinates(navigationRequest.getDest_x(), navigationRequest.getDest_y());
        if(destNode.isEmpty()){
            logger.error("No Node at destination coordinates! {}", destNode);
        }

        return  dijkstraSearch(startNode.get(), destNode.get());

    }

    private List<Node> dijkstraSearch(Node start, Node dest){
        DirectedWeightedMultigraph<Node, DefaultWeightedEdge> map = manager.getGraph();
        GraphPath<Node, DefaultWeightedEdge> resultPath = BidirectionalDijkstraShortestPath.findPathBetween(map, start, dest);

        return  resultPath.getVertexList();
    }

    protected Optional<Node> findNodeInGraphWithCoordinates(Integer x, Integer y){

        Optional<Node> node = manager.getGraph().vertexSet().stream()
                .filter(n -> ((x <= n.getX()+configuration.getNodeToleranceX()
                                && x >= n.getX()-configuration.getNodeToleranceX())
                        && (y <= n.getY()+configuration.getNodeToleranceY()
                                && y >= n.getY()-configuration.getNodeToleranceY()))).findFirst();

        return node;
    }
}


