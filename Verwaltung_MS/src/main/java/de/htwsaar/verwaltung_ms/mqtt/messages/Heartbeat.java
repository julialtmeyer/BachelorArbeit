package de.htwsaar.verwaltung_ms.mqtt.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwsaar.verwaltung_ms.deserializer.HeartbeatDeserializer;

@JsonDeserialize(using = HeartbeatDeserializer.class)
public class Heartbeat {

    private Long id;
    private String name;
    private String hsc;
    private String macAdr;

    public Heartbeat() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setMacAdr(String macAdr) {
        this.macAdr = macAdr;
    }

    @Override
    public String toString() {
        return "Heartbeat{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hsc='" + hsc + '\'' +
                ", macAdr='" + macAdr + '\'' +
                '}';
    }
}
