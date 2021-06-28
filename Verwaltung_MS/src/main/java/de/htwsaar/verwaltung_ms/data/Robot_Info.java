package de.htwsaar.verwaltung_ms.data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ROBOT_INFO")
public class Robot_Info {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "robot_id")
    private Robot robot;

    @Column(name = "battery")
    private Integer battery;

    @Column(name = "last_picture", columnDefinition = "MEDIUMBLOB")
    private String last_picture;

    @Column(name = "x_coord")
    private Integer x_coord;

    @Column(name = "y_coord")
    private Integer y_coord;

    public Robot_Info() {
    }

    public Robot_Info(Long id, Robot robot, Integer x_coord, Integer y_coord) {
        this.id = id;
        this.robot = robot;
        this.x_coord = x_coord;
        this.y_coord = y_coord;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot_Info that = (Robot_Info) o;
        return id.equals(that.id) && robot.equals(that.robot) && Objects.equals(battery, that.battery) && Objects.equals(last_picture, that.last_picture) && Objects.equals(x_coord, that.x_coord) && Objects.equals(y_coord, that.y_coord);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, robot, battery, last_picture, x_coord, y_coord);
    }

    @Override
    public String toString() {
        return "Robot_Info{" +
                "id=" + id +
                ", robot_id=" + robot +
                ", battery=" + battery +
                ", last_picture='" + last_picture + '\'' +
                ", x_coord=" + x_coord +
                ", y_coord=" + y_coord +
                '}';
    }
}
