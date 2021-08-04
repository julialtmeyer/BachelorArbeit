package de.htwsaar.backend_service.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class TurnCommand extends Command{

    public TurnCommand(Double direction) {
        super(direction);
    }
}
