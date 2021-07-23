package de.htwsaar.navigation_service.map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

public class CorrelationNode extends Node{

    private Node correlation;
    private Node previousNode;
    private Node nextNode;

    public CorrelationNode(@JsonProperty("id") Integer id,@JsonProperty("x") double x,@JsonProperty("y") double y,
                           @JsonProperty("weight") Integer weight,
                           @JsonProperty("previousNode") Node previousNode,
                           @JsonProperty("nextNode") Node nextNode) {
        super(id, x, y, weight);
        this.previousNode = previousNode;
        this.nextNode = nextNode;
    }

    @JsonCreator
    public CorrelationNode(@JsonProperty("id") Integer id,@JsonProperty("x") double x,@JsonProperty("y") double y,
                           @JsonProperty("weight") Integer weight,
                           @JsonProperty("correlation") Node correlation,
                           @JsonProperty("previousNode") Node previousNode,
                           @JsonProperty("nextNode") Node nextNode) {
        super(id, x, y, weight);
        this.correlation = correlation;
        this.previousNode = previousNode;
        this.nextNode = nextNode;
    }

    @JsonIgnore
    public Node getCorrelation() {
        return correlation;
    }

    public void setCorrelation(Node correlation) {
        this.correlation = correlation;
    }

    @JsonIgnore
    public Node getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(Node previousNode) {
        this.previousNode = previousNode;
    }

    @JsonIgnore
    public Node getNextNode() {
        return nextNode;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    @Override
    public String toString() {
        return "CorrelationNode{" +
                "id=" + super.getId() +
                ", x=" + super.getX() +
                ", y=" + super.getY() +
                ", weight=" + super.getWeight() +
                "correlation=" + correlation +
                ", previousNode=" + previousNode +
                ", nextNode=" + nextNode +
                '}';
    }
}
