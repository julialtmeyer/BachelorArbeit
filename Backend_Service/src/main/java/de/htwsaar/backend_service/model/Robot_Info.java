package de.htwsaar.backend_service.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ROBOT_INFO")
public class Robot_Info {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "robot_id", nullable = false, unique = true)
    private Robot robot;

    @Column(name = "battery")
    private Double battery;

    @Column(name = "last_picture", columnDefinition = "MEDIUMBLOB")
    private String last_picture;

    @Column(name = "location_x")
    private Double location_x;

    @Column(name = "location_y")
    private Double location_y;

    @Column(name = "direction_x")
    private Double direction_x;

    @Column(name = "direction_y")
    private Double direction_y;



    public Robot_Info() {
    }

    public Robot_Info(Long id, Robot robot, Double x_coord, Double y_coord) {
        this.id = id;
        this.robot = robot;
        this.location_x = x_coord;
        this.location_y = y_coord;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Robot getRobot() {
        return robot;
    }

    public void setRobot(Robot robot_id) {
        this.robot = robot_id;
    }

    public Double getBattery() {
        return battery;
    }

    public void setBattery(Double battery) {
        this.battery = battery;
    }

    public String getLast_picture() {
        return last_picture;
    }

    public void setLast_picture(String last_picture) {
        this.last_picture = last_picture;
    }

    public Double getLocation_x() {
        return location_x;
    }

    public void setLocation_x(Double x_coord) {
        this.location_x = x_coord;
    }

    public Double getLocation_y() {
        return location_y;
    }

    public void setLocation_y(Double y_coord) {
        this.location_y = y_coord;
    }

    public Double getDirection_x() {
        return direction_x;
    }

    public void setDirection_x(Double direction_x) {
        this.direction_x = direction_x;
    }

    public Double getDirection_y() {
        return direction_y;
    }

    public void setDirection_y(Double direction_y) {
        this.direction_y = direction_y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot_Info that = (Robot_Info) o;
        return id.equals(that.id) && robot.equals(that.robot) && Objects.equals(battery, that.battery) && Objects.equals(last_picture, that.last_picture) && Objects.equals(location_x, that.location_x) && Objects.equals(location_y, that.location_y) && Objects.equals(direction_x, that.direction_x) && Objects.equals(direction_y, that.direction_y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, robot, battery, last_picture, location_x, location_y, direction_x, direction_y);
    }

    @Override
    public String toString() {
        return "Robot_Info{" +
                "id=" + id +
                ", robot_id=" + robot +
                ", battery=" + battery +
                ", last_picture='" + last_picture + '\'' +
                ", location_x=" + location_x +
                ", location_y=" + location_y +
                ", direction_x=" + direction_x +
                ", direction_y=" + direction_y +
                '}';
    }
}
