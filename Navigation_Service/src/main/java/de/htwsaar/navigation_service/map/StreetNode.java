package de.htwsaar.navigation_service.map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class StreetNode extends Node{


    private List<Node> neighbors;


    public StreetNode(@JsonProperty("id") Integer id,@JsonProperty("x") double x,@JsonProperty("y") double y,
                      @JsonProperty("weight") Integer weight,
                      @JsonProperty("neighbors") List<Node> neighbors) {
        super(id, x, y, weight);
        this.neighbors = neighbors;
    }

    public StreetNode(Integer id, double x, double y, Integer weight) {
        super(id, x, y, weight);
        this.neighbors = new ArrayList<>();
    }

    @JsonIgnore
    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    @Override
    public String toString() {
        return "StreetNode{" +
                "id=" + super.getId() +
                ", x=" + super.getX() +
                ", y=" + super.getY() +
                ", weight=" + super.getWeight() +
                ", neighbors=" + neighbors +
                '}';
    }
}
