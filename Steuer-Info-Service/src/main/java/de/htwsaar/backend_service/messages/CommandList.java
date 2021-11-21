package de.htwsaar.backend_service.messages;

import java.util.List;

public class CommandList {
    public List<Command> commands;

    public CommandList(List<Command> list){
        this.commands = list;
    }
}
