package de.htwsaar.navigation_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mxgraph.layout.*;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.swing.mxGraphComponent;
import de.htwsaar.navigation_service.map.*;
import org.apache.commons.math3.analysis.interpolation.LinearInterpolator;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import javax.swing.*;
import java.util.ArrayList;

@SpringBootTest
public class MapTest {

    @Test
    public void testGraphToJSON(){
        MapGraph graph = new MapGraph();
        graph.setName("htw");
        graph.setId(0);
        ArrayList<Node> list = new ArrayList();

        Node streetNode = new Node(0, 0, 0);
        Node streetNode1 = new Node(1, 2,2);
        Node destinationNode = new Node(5,5,5);
        Node correlationNode = new Node(2, 5,6);
        destinationNode.getNeighbors().add(correlationNode);

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
            String control = "{\"name\":\"htw\",\"id\":0,\"nodes\":[{\"id\":0,\"x\":0.0,\"y\":0.0},{\"id\":1,\"x\":2.0,\"y\":2.0},{\"id\":2,\"x\":5.0,\"y\":6.0},{\"id\":5,\"x\":5.0,\"y\":5.0}]}";
            String map = mapper.writeValueAsString(graph);
            assert(map.equals(control));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLinearInterpolator(){
        double x[] = new double[2];
        double y[] = new double[2];

        x[0] = 596;
        x[1] = 1007;
        y[0] = 577;
        y[1] = 444;

        LinearInterpolator linearInterpolator = new LinearInterpolator();
        PolynomialSplineFunction splineFunction = linearInterpolator.interpolate(x, y);
        double start = x[0];
        while (start <= x[1]){

            System.out.println(start);
            start = start +5;

        }
    }
}
