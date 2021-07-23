package de.htwsaar.navigation_service.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Set;

@Component
public class MapManager {

    private DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph;

    public MapManager() throws Exception, IOException {
        this.graph = readMap();
    }

    public DirectedWeightedMultigraph<Node, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    private DirectedWeightedMultigraph<Node, DefaultWeightedEdge> readMap() throws IOException, Exception {

        graph = new DirectedWeightedMultigraph<>(DefaultWeightedEdge.class);
        ClassPathResource resource = new ClassPathResource("map.json");
        InputStream inputStream = resource.getInputStream();
        BufferedReader bufferReader = new BufferedReader (new InputStreamReader(inputStream));
        StringBuilder stringBuilder = new StringBuilder();
        String eachStringLine;
        while((eachStringLine=bufferReader.readLine()) != null){
            stringBuilder.append(eachStringLine).append("\n");
        }
        String mapString = stringBuilder.toString();

        try {

            ObjectMapper mapper = new ObjectMapper();
            MapGraph mapGraph = mapper.readValue(mapString, MapGraph.class);

            for(Node node : mapGraph.getNodes()) {
                graph.addVertex(node);
            }

            for(Node node : mapGraph.getNodes()){

                if(node.getClass().equals(StreetNode.class) && !((StreetNode) node).getNeighbors().isEmpty()){
                    for (Node neighbor : ((StreetNode) node).getNeighbors()){
                        neighbor = getNodeById(neighbor.getId());
                        graph.addEdge(node, neighbor);
                        graph.setEdgeWeight(node, neighbor, getDistanceToNode(node, neighbor));
                    }
                }
                else if(node.getClass().equals(CorrelationNode.class)){
                    Node correlation = getNodeById(((CorrelationNode) node).getCorrelation().getId());
                    Node prev = getNodeById(((CorrelationNode) node).getPreviousNode().getId());
                    Node next = getNodeById(((CorrelationNode) node).getNextNode().getId());

                    graph.addEdge(node, correlation);
                    graph.setEdgeWeight(node, correlation, getDistanceToNode(node, correlation));

                    graph.addEdge(node, prev);
                    graph.setEdgeWeight(node, prev, getDistanceToNode(node, prev));

                    graph.addEdge(node, next);
                    graph.setEdgeWeight(node, next, getDistanceToNode(node, next));
                }
                else {
                    Node entry = getNodeById(((DestinationNode) node).getEntryNode().getId());
                    graph.addEdge(node, entry);
                    graph.setEdgeWeight(node, entry, getDistanceToNode(node, entry));
                }
            }

        } catch (Exception e){
            throw new Exception("map is malformed " + e.getMessage());
        }

        return graph;
    }

    /**
     * Gibt einen Node anhand der ID aus der Karte zurück
     * @param id
     * @return Node
     */
    private Node getNodeById(Integer id){

        Set<Node> nodes = graph.vertexSet();

        for(Node node : nodes){
            if(node.getId().equals(id)){
                return node;
            }

        }

        throw new NoSuchElementException("Node " + id + " does not exist");
    }

    private Double getDistanceToNode(Node n1, Node n2){
        Double distance;
        Double x = Math.abs(n1.getX() - n2.getX());
        Double y = Math.abs(n1.getY() - n2.getY());

        if(x == 0.0 || y == 0.0){
            distance = x + y;
        }
        else {
            x = x * x;
            y = y * y;

            distance = Math.sqrt(x * y);
        }


        return distance;
    }
}
