package de.htwsaar.backend_service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.htwsaar.backend_service.messages.Command;
import de.htwsaar.backend_service.messages.DriveCommand;
import de.htwsaar.backend_service.messages.TurnCommand;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CommandTestJSON {


    @Test
    public void testJSONMappingOfDriveCommand(){
        Command command = new DriveCommand(10);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String manualJson = "{\"DriveCommand\":{\"direction\":10}}";
            String mappedJson = mapper.writeValueAsString(command);
            assert manualJson.equals(mappedJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJSONMappingOfTurnCommand(){
        Command command = new TurnCommand(10);
        ObjectMapper mapper = new ObjectMapper();

        try {
            String manualJson = "{\"TurnCommand\":{\"direction\":10}}";
            String mappedJson = mapper.writeValueAsString(command);
            assert manualJson.equals(mappedJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
