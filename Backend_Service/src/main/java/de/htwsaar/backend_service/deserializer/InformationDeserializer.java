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

        JsonNode jsonNode = p.getCodec().readTree(p);
        JsonNode informationNode = jsonNode.get("information");
        InformationMessage info = new InformationMessage();
        try {
            info.setBattery(informationNode.get("battery").asText());
            info.setPicture(informationNode.get("pic").asText());
            info.setX_coord(informationNode.get("x").asInt());
            info.setY_coord(informationNode.get("y").asInt());
        }
        catch (NullPointerException e) {
            System.err.println("Error while reading Information! Payload does not match the requirements.");
        }
        return info;
    }
}
