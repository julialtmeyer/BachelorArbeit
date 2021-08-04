package de.htwsaar.backend_service.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

public class DriveCommand extends Command{

    public DriveCommand(Double direction) {
        super(direction);
    }

}
