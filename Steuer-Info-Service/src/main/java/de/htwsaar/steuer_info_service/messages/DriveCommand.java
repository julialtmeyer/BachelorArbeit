package de.htwsaar.steuer_info_service.messages;

public class DriveCommand extends Command{

    public DriveCommand(Double direction) {
        super(direction);
    }

    @Override
    public String toString() {
        return "DriveCommand{" +
                "direction=" + super.getDirection() +
                '}';
    }
}
