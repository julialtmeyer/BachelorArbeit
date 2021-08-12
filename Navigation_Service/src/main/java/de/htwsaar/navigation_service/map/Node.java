package de.htwsaar.navigation_service.map;

import com.fasterxml.jackson.annotation.*;

import java.util.LinkedList;
import java.util.List;

public class Node {

    private Integer id;
    private Double x;
    private Double y;
    private List<Node> neighbors;

    @JsonCreator
    public Node(@JsonProperty("id") Integer id,@JsonProperty("x") double x,@JsonProperty("y") double y,@JsonProperty("neighbors") List<Node> neighbors) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.neighbors = neighbors;
    }

    public Node(@JsonProperty("id") Integer id,@JsonProperty("x") double x,@JsonProperty("y") double y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.neighbors = new LinkedList<>();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @JsonIgnore
    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(Node neighbor) {
        this.neighbors.add(neighbor);
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
