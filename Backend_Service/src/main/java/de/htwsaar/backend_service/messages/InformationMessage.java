package de.htwsaar.backend_service.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class InformationMessage {

    private Integer battery;
    private String picture;
    private Double x_coord;
    private Double y_coord;

    public InformationMessage() {
    }

    @JsonCreator
    public InformationMessage(@JsonProperty("battery") Integer battery, @JsonProperty("picture") String picture,
                              @JsonProperty("x_coord") Double x_coord, @JsonProperty("y_coord") Double y_coord) {
        this.battery = battery;
        this.picture = picture;
        this.x_coord = x_coord;
        this.y_coord = y_coord;

    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Double getX_coord() {
        return x_coord;
    }

    public void setX_coord(Double x_coord) {
        this.x_coord = x_coord;
    }

    public Double getY_coord() {
        return y_coord;
    }

    public void setY_coord(Double y_coord) {
        this.y_coord = y_coord;
    }

    @Override
    public String toString() {
        return "InformationMessage{" +
                "battery='" + battery + '\'' +
                ", picture='" + picture + '\'' +
                ", x_coord=" + x_coord +
                ", y_coord=" + y_coord +
                '}';
    }
}
