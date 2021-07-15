package de.htwsaar.backend_service.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwsaar.backend_service.deserializer.InformationDeserializer;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonDeserialize(using = InformationDeserializer.class)
public class InformationMessage {

    private Integer battery;
    private String picture;
    private Integer x_coord;
    private Integer y_coord;

    public InformationMessage() {
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

    public Integer getX_coord() {
        return x_coord;
    }

    public void setX_coord(Integer x_coord) {
        this.x_coord = x_coord;
    }

    public Integer getY_coord() {
        return y_coord;
    }

    public void setY_coord(Integer y_coord) {
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
