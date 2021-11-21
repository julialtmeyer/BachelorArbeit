package de.htwsaar.backend_service.http;

import java.io.Serializable;

public class NavigationPost implements Serializable {

    private Double start_x;
    private Double start_y;
    private Double dest_x;
    private Double dest_y;

    public NavigationPost(Double start_x, Double start_y, Double dest_x, Double dest_y) {
        this.start_x = start_x;
        this.start_y = start_y;
        this.dest_x = dest_x;
        this.dest_y = dest_y;
    }

    public Double getStart_x() {
        return start_x;
    }

    public void setStart_x(Double start_x) {
        this.start_x = start_x;
    }

    public Double getStart_y() {
        return start_y;
    }

    public void setStart_y(Double start_y) {
        this.start_y = start_y;
    }

    public Double getDest_x() {
        return dest_x;
    }

    public void setDest_x(Double dest_x) {
        this.dest_x = dest_x;
    }

    public Double getDest_y() {
        return dest_y;
    }

    public void setDest_y(Double dest_y) {
        this.dest_y = dest_y;
    }
}
