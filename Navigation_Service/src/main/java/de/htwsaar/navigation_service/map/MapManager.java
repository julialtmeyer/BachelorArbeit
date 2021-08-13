package de.htwsaar.navigation_service.map;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.navigation_service.Configuration;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.traverse.BreadthFirstIterator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Component
public class MapManager {

    private DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph;

    private MapRepository mapRepository;

    private Configuration config;

    public MapManager(MapRepository mapRepository, Configuration config) throws Exception, IOException {
        this.mapRepository = mapRepository;
        this.config = config;
        this.graph = readMap();
        fillGrid(this.graph);
    }

    public DirectedWeightedMultigraph<Node, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    /**
     * Method to initialize the map for the service upon startup
     * tries to get the map from the database
     * map is stored in a JSON format
     * @return returns a DirectedWeightedMultigraph
     * @throws IOException when there is an error while reading the JSON
     * @throws Exception when there is an error while building the graph
     */
    private DirectedWeightedMultigraph<Node, DefaultWeightedEdge> readMap() throws IOException, Exception {

        String mapString;
        graph = new DirectedWeightedMultigraph<>(DefaultWeightedEdge.class);

        Optional<Map> map = mapRepository.getMapById(config.getMapId());
        if(map.isEmpty()){

            ClassPathResource resource = new ClassPathResource("map.json");
            InputStream inputStream = resource.getInputStream();
            BufferedReader bufferReader = new BufferedReader (new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String eachStringLine;
            while((eachStringLine=bufferReader.readLine()) != null){
                stringBuilder.append(eachStringLine).append("\n");
            }
            mapString = stringBuilder.toString();
        }
        else {
            mapString = map.get().getMapJSON();
        }

        try {

            ObjectMapper mapper = new ObjectMapper();
            MapGraph mapGraph = mapper.readValue(mapString, MapGraph.class);

            for(Node node : mapGraph.getNodes()) {
                graph.addVertex(node);
            }

            for(Node node : mapGraph.getNodes()){

                    for (Node neighbor : node.getNeighbors()){
                        neighbor = getNodeById(neighbor.getId());
                        graph.addEdge(node, neighbor);
                        graph.setEdgeWeight(node, neighbor, getDistanceToNode(node, neighbor));
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

    /**
     * Method to calculate the distance between two nodes
     * @param n1 first node
     * @param n2 second node
     * @return distance between the two nodes
     */
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

    /**
     * Method to interpolate nodes between nodes of a graph.
     * uses LinearInterpolator of math3 library for functions
     * iterates over X or Y when X or Y of source and target nodes are the same
     * @param graph the graph that has to be interpolated
     */
    public void fillGrid(DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph) {

        //graph needs to be cloned, no changes are allowed to the graph when it is iterated upon
        DirectedWeightedMultigraph<Node, DefaultWeightedEdge> iteratorGraph = (DirectedWeightedMultigraph<Node, DefaultWeightedEdge>) graph.clone();
        BreadthFirstIterator<Node, DefaultWeightedEdge> iterator = new BreadthFirstIterator<>(iteratorGraph);
        while (iterator.hasNext()) {
            Node current = iterator.next();
            DefaultWeightedEdge edge = iterator.getSpanningTreeEdge(current);
            if (edge != null) {
                Node source = iteratorGraph.getEdgeSource(edge);
                Node target = iteratorGraph.getEdgeTarget(edge);

                //when X or Y of the source and target are the same it is impossible to calculate a corresponding function
                if (source.getX().doubleValue() != target.getX().doubleValue() && source.getY().doubleValue() != target.getY().doubleValue()) {

                    double x[] = new double[2];
                    double y[] = new double[2];
                    if(source.getX() < target.getX()){
                        x[0] = source.getX();
                        x[1] = target.getX();
                        y[0] = source.getY();
                        y[1] = target.getY();
                    }
                    else {
                        x[0] = target.getX();
                        x[1] = source.getX();
                        y[0] = target.getY();
                        y[1] = source.getY();
                    }


                    LinearInterpolator linearInterpolator = new LinearInterpolator();
                    PolynomialSplineFunction splineFunction = linearInterpolator.interpolate(x, y);

                    Node prevNeighbor = source;
                    double v = source.getX();
                    int i = 0;
                    while (v <= target.getX()){

                        prevNeighbor = getNode(graph, source, target, v, prevNeighbor, i, false, true, splineFunction);
                        v = v + config.getGridSpacing();
                        i++;
                        last(graph, target, prevNeighbor, v <= target.getX());
                    }

                /*  source X == target X
                    Y Coordinate is iterated
                */
                } else if (source.getX().doubleValue() == target.getX().doubleValue()) {
                    double v = source.getY();
                    Node prevNeighbor = source;
                    int i = 0;
                    if (source.getY() < target.getY()) {
                        while (v <= target.getY()) {

                            prevNeighbor = getNode(graph, source, target, v, prevNeighbor, i, true, false, null);
                            v = v + config.getGridSpacing();
                            i++;
                            last(graph, target, prevNeighbor, v <= target.getY());
                        }
                    } else {
                        while (v >= target.getY()) {

                            prevNeighbor = getNode(graph, source, target, v, prevNeighbor, i, true, false, null);
                            v = v - config.getGridSpacing();
                            i++;
                            last(graph, target, prevNeighbor, v >= target.getY());
                        }
                    }

                /*  source Y == target Y
                    X Coordinate is iterated
                 */
                } else {
                    double v = source.getX();
                    Node prevNeighbor = source;
                    int i = 0;
                    if (source.getX() < target.getX()) {
                        while (v <= target.getX()) {

                            prevNeighbor = getNode(graph, source, target, v, prevNeighbor, i, false, false, null);
                            v = v + config.getGridSpacing();
                            i++;
                            last(graph, target, prevNeighbor, v <= target.getY());
                        }
                    } else {
                        while (v >= target.getX()) {

                            prevNeighbor = getNode(graph, source, target, v, prevNeighbor, i, false, false, null);
                            v = v - config.getGridSpacing();
                            i++;
                            last(graph, target, prevNeighbor, v >= target.getY());
                        }
                    }
                }
                //graph.removeEdge(source, target);
            }
        }
    }

    /**
     * Method to add edge between last interpolated node and the original target node
     * @param graph the graph that is to be edited
     * @param target Node
     * @param prevNeighbor Node
     * @param condition the condition to determine if the last node is reached
     */
    private void last(DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph, Node target,Node prevNeighbor, boolean condition) {
        if (!condition) {
            graph.addEdge(prevNeighbor, target);
            graph.setEdgeWeight(prevNeighbor, target, getDistanceToNode(prevNeighbor, target));
        }
    }

    /**
     * Method to add interpolated node and corresponding edges to a graph
     * @param graph the graph that is to be edited
     * @param source Node
     * @param target Node
     * @param v x or y coordinate
     * @param prevNeighbor Node
     * @param i iterator for generating a new NodeID
     * @param xIsSame true when X of source and target are the same
     * @param isSloped false when line is horizontal or vertical
     * @param function only needed when isSloped is true
     * @return interpolated Node
     */
    private Node getNode(DirectedWeightedMultigraph<Node, DefaultWeightedEdge> graph, Node source, Node target, double v, Node prevNeighbor, int i, boolean xIsSame, boolean isSloped, PolynomialSplineFunction function) {
        Integer id = (source.getId() + target.getId() * 20) + i;
        Node newNode;
        if(isSloped){
            newNode = new Node(id, v, function.value(v));
        }
        else if(xIsSame){
            newNode = new Node(id, source.getX(), v);
        }
        else {
            newNode = new Node(id, v, source.getY());
        }
        if(newNode.getX().doubleValue() == source.getX().doubleValue() && newNode.getY().doubleValue() == source.getY().doubleValue()){
            return prevNeighbor;
        }
        graph.addVertex(newNode);
        graph.addEdge(prevNeighbor, newNode);
        graph.setEdgeWeight(prevNeighbor, newNode, getDistanceToNode(prevNeighbor, newNode));
        prevNeighbor = newNode;
        return prevNeighbor;
    }

    public boolean addNewMap(String map_json){

        Map map = new Map();
        map.setMapJSON(map_json);

        Map newMap = mapRepository.save(map);

        return true;
    }
}
