package de.htwsaar.backend_service.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.htwsaar.backend_service.messages.InformationMessage;

import java.io.IOException;

public class InformationDeserializer extends StdDeserializer<InformationMessage> {

    public InformationDeserializer(){
        this(null);
    }

    public InformationDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public InformationMessage deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {

        JsonNode informationNode = p.getCodec().readTree(p);
        InformationMessage info = new InformationMessage();
        try {
            if(informationNode.get("battery").canConvertToInt()){
                info.setBattery(informationNode.get("battery").asInt());
            }
            else if(informationNode.get("battery").isTextual()){
                info.setBattery(Integer.valueOf(informationNode.get("battery").asText()));
            }
            info.setBattery(informationNode.get("battery").asInt());
            info.setPicture(informationNode.get("picture").asText());
            info.setX_coord(informationNode.get("x_coord").asInt());
            info.setY_coord(informationNode.get("y_coord").asInt());
        }
        catch (Exception e) {
            System.err.println("Error while reading Information! Payload does not match the requirements.");
        }
        return info;
    }
}
