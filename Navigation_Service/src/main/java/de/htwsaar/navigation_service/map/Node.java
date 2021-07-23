package de.htwsaar.navigation_service.map;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = StreetNode.class, name = "StreetNode"),
        @JsonSubTypes.Type(value = DestinationNode.class, name = "DestinationNode"),
        @JsonSubTypes.Type(value = CorrelationNode.class, name = "CorrelationNode")
})
public abstract class Node {

    private Integer id;
    private Double x;
    private Double y;
    private Integer weight;

    public Node(@JsonProperty("id") Integer id,@JsonProperty("x") double x,@JsonProperty("y") double y,
                @JsonProperty("weight") Integer weight) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.weight = weight;
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

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
