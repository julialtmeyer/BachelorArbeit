package de.htwsaar.steuer_info_service.messages;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
public class InformationMessage {

    private Double battery;
    private String picture;
    private Double x_coord;
    private Double y_coord;
    private Double orientation;

    public InformationMessage() {
    }

    @JsonCreator
    public InformationMessage(@JsonProperty("battery") Double battery, @JsonProperty("picture") String picture,
                              @JsonProperty("x_coord") Double x_coord, @JsonProperty("y_coord") Double y_coord,
                              @JsonProperty("orientation") Double orientation) {
        this.battery = battery;
        this.picture = picture;
        this.x_coord = x_coord;
        this.y_coord = y_coord;
        this.orientation = orientation;

    }

    public Double getBattery() {
        return battery;
    }

    public void setBattery(Double battery) {
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

    public Double getOrientation() {
        return orientation;
    }

    public void setOrientation(Double orientation) {
        this.orientation = orientation;
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
