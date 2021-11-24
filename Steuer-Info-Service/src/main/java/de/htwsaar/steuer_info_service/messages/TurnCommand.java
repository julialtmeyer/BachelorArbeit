package de.htwsaar.steuer_info_service.messages;

public class TurnCommand extends Command{

    public TurnCommand(Double direction) {
        super(direction);
    }

    @Override
    public String toString() {
        return "TurnCommand{" +
                "direction=" + super.getDirection() +
                '}';
    }
}
