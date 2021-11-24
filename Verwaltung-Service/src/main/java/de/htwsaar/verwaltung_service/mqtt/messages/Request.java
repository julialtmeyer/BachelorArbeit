package de.htwsaar.verwaltung_service.mqtt.messages;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import de.htwsaar.verwaltung_service.deserializer.RequestDeserializer;

@JsonDeserialize(using = RequestDeserializer.class)
public class Request{

    private String hsc;
    private String macAdr;

    public Request() {
    }

    public Request(String hsc, String macAdr) {
        this.hsc = hsc;
        this.macAdr = macAdr;
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
        return "Request{" +
                "hsc='" + hsc + '\'' +
                ", macAdr='" + macAdr + '\'' +
                '}';
    }
}
