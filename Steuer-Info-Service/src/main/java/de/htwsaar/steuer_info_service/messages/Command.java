package de.htwsaar.steuer_info_service.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.WRAPPER_OBJECT)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DriveCommand.class, name = "DriveCommand"),
        @JsonSubTypes.Type(value = TurnCommand.class, name = "TurnCommand")
})
public abstract class Command {

    private Double direction;

    public Command(@JsonProperty("direction") Double direction) {
        this.direction = direction;
    }

    public Double getDirection() {
        return direction;
    }

    public void setDirection(Double direction) {
        this.direction = direction;
    }
}
