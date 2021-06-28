package de.htwsaar.verwaltung_ms.data;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ACTIVE_ROBOTS")
public class Robot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "robot_name", unique = true, nullable = false)
    private String roboterName;

    @Column(name = "handshake_code", unique = true, nullable = false)
    private Long hsc;

    @Column(name = "mac_adr", unique = true, nullable = false)
    private String mac_adr;

    @OneToOne(mappedBy = "robot")
    private Robot_Info robot_info;

    public Robot() {
    }

    public Robot(Long id, String roboterName, Long hsc, String mac_adr) {
        this.id = id;
        this.roboterName = roboterName;
        this.hsc = hsc;
        this.mac_adr = mac_adr;
    }

    public Robot(Long id, String roboterName, Long hsc, String mac_adr, Robot_Info robot_info) {
        this.id = id;
        this.roboterName = roboterName;
        this.hsc = hsc;
        this.mac_adr = mac_adr;
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

    public Long getHsc() {
        return hsc;
    }

    public void setHsc(Long hsc) {
        this.hsc = hsc;
    }

    public String getMac_adr() {
        return mac_adr;
    }

    public void setMac_adr(String mac_adr) {
        this.mac_adr = mac_adr;
    }

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
        return id.equals(robot.id) && roboterName.equals(robot.roboterName) && hsc.equals(robot.hsc) && mac_adr.equals(robot.mac_adr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roboterName, hsc, mac_adr);
    }

    @Override
    public String toString() {
        return "Robot{" +
                "id=" + id +
                ", roboterName='" + roboterName + '\'' +
                ", hsc=" + hsc +
                ", mac_adr='" + mac_adr + '\'' +
                '}';
    }
}