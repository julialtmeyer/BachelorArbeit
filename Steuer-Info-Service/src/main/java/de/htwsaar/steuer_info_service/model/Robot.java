package de.htwsaar.steuer_info_service.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Entity
@Table(name = "ROBOTS")
public class Robot {

    @Id
    @Column(name = "id")
    public Long id;

    @Column(name = "robot_name", unique = true, nullable = false)
    private String roboterName;

    @Column(name = "handshake_code", unique = true, nullable = false)
    private String hsc;

    @Column(name = "mac_adr", unique = true, nullable = false)
    private String macAdr;

    @Column(name = "active")
    private boolean isActive;

    @Column(name = "last_active")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @JsonIgnore
    private Instant lastActive;

    @OneToOne(mappedBy = "robot")
    private Robot_Info robot_info;

    public Robot() {
    }

    public Robot(String roboterName, String hsc, String macAdr) {
        this.roboterName = roboterName;
        this.hsc = hsc;
        this.macAdr = macAdr;
    }

    public Robot(String roboterName, String hsc, String macAdr, Robot_Info robot_info) {
        this.roboterName = roboterName;
        this.hsc = hsc;
        this.macAdr = macAdr;
        this.robot_info = robot_info;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoboterName() {
        return roboterName;
    }

    public void setRoboterName(String roboterName) {
        this.roboterName = roboterName;
    }

    public String getHsc() {
        return hsc;
    }

    public void setHsc(String hsc) {
        this.hsc = hsc;
    }

    public String getMacAdr() {
        return macAdr;
    }

    public void setMacAdr(String mac_adr) {
        this.macAdr = mac_adr;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Instant getLastActive() {
        return lastActive;
    }

    public void setLastActive(Instant lastActive) {
        this.lastActive = lastActive;
    }

    @JsonIgnoreProperties({"id", "robot", "last_picture"})
    public Robot_Info getRobot_info() {
        return robot_info;
    }

    public void setRobot_info(Robot_Info robot_info) {
        this.robot_info = robot_info;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Robot robot = (Robot) o;
        return id.equals(robot.id) && roboterName.equals(robot.roboterName) && hsc.equals(robot.hsc) && macAdr.equals(robot.macAdr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roboterName, hsc, macAdr);
    }

    @Override
    public String toString() {
        return "Robot{" +
                "id=" + id +
                ", roboterName='" + roboterName + '\'' +
                ", hsc='" + hsc + '\'' +
                ", macAdr='" + macAdr + '\'' +
                ", isActive=" + isActive +
                ", lastActive=" + lastActive +
                '}';
    }
}