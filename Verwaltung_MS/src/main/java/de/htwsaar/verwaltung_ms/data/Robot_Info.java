package de.htwsaar.verwaltung_ms.data;

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
    private Integer battery;

    @Column(name = "last_picture", columnDefinition = "MEDIUMBLOB")
    private String last_picture;

    @Column(name = "location_x")
    private Integer location_x;

    @Column(name = "location_y")
    private Integer location_y;

    @Column(name = "direction_x")
    private Integer direction_x;

    @Column(name = "direction_y")
    private Integer direction_y;



    public Robot_Info() {
    }

    public Robot_Info(Long id, Robot robot, Integer x_coord, Integer y_coord) {
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

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public String getLast_picture() {
        return last_picture;
    }

    public void setLast_picture(String last_picture) {
        this.last_picture = last_picture;
    }

    public Integer getLocation_x() {
        return location_x;
    }

    public void setLocation_x(Integer x_coord) {
        this.location_x = x_coord;
    }

    public Integer getLocation_y() {
        return location_y;
    }

    public void setLocation_y(Integer y_coord) {
        this.location_y = y_coord;
    }

    public Integer getDirection_x() {
        return direction_x;
    }

    public void setDirection_x(Integer direction_x) {
        this.direction_x = direction_x;
    }

    public Integer getDirection_y() {
        return direction_y;
    }

    public void setDirection_y(Integer direction_y) {
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
