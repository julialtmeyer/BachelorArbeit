package de.htwsaar.navigation_service.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DestinationNode extends Node{

    private Node entryNode;

    public DestinationNode(@JsonProperty("id") Integer id, @JsonProperty("x") double x, @JsonProperty("y") double y,
                           @JsonProperty("weight") Integer weight) {
        super(id, x, y, weight);
    }

    @JsonCreator
    public DestinationNode(@JsonProperty("id") Integer id, @JsonProperty("x") double x, @JsonProperty("y") double y,
                           @JsonProperty("weight") Integer weight,
                           @JsonProperty("entryNode") Node entryNode) {
        super(id, x, y, weight);
        this.entryNode = entryNode;
    }

    @JsonIgnore
    public Node getEntryNode() {
        return entryNode;
    }

    public void setEntryNode(Node entryNode) {
        this.entryNode = entryNode;
    }

    @Override
    public String toString() {
        return "DestinationNode{" +
                "id=" + super.getId() +
                ", x=" + super.getX() +
                ", y=" + super.getY() +
                ", weight=" + super.getWeight() +
                "entryNode=" + entryNode +
                '}';
    }
}
