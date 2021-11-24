package de.htwsaar.verwaltung_service.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.htwsaar.verwaltung_service.mqtt.messages.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class RequestDeserializer extends StdDeserializer<Request> {

    private final Logger logger = LoggerFactory.getLogger(RequestDeserializer.class);

    public RequestDeserializer(){
        this(null);
    }

    public RequestDeserializer(Class<?> vc){
        super(vc);
    }

    @Override
    public Request deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode jsonNode = p.getCodec().readTree(p);
        JsonNode requestNode = jsonNode.get("request");
        Request request = new Request();
        try {
            request.setHsc(requestNode.get("hsc").asText());
            request.setMacAdr(requestNode.get("macAdr").asText());
        }
        catch (NullPointerException e){
            logger.error("Error while reading Request! Payload does not match the requirements. {}", jsonNode, e);
        }

        return request;
    }
}
