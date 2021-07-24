package de.htwsaar.navigation_service;

import com.fasterxml.jackson.annotation.JsonProperty;

public class NavigationRequest {
    private Integer start_x;
    private Integer start_y;
    private Integer dest_x;
    private Integer dest_y;

    public NavigationRequest() {
    }

    public NavigationRequest(@JsonProperty("start_x") Integer start_x,@JsonProperty("start_y") Integer start_y,
                             @JsonProperty("dest_x") Integer dest_x,@JsonProperty("dest_y") Integer dest_y) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.dest_x = dest_x;
        this.dest_y = dest_y;
    }

    public Integer getStart_x() {
        return start_x;
    }

    public void setStart_x(Integer start_x) {
        this.start_x = start_x;
    }

    public Integer getStart_y() {
        return start_y;
    }

    public void setStart_y(Integer start_y) {
        this.start_y = start_y;
    }

    public Integer getDest_x() {
        return dest_x;
    }

    public void setDest_x(Integer dest_x) {
        this.dest_x = dest_x;
    }

    public Integer getDest_y() {
        return dest_y;
    }

    public void setDest_y(Integer dest_y) {
        this.dest_y = dest_y;
    }
}
