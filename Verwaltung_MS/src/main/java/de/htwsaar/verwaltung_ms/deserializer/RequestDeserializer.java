package de.htwsaar.verwaltung_ms.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.htwsaar.verwaltung_ms.mqtt.messages.Request;

import java.io.IOException;

public class RequestDeserializer extends StdDeserializer<Request> {

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
        request.setHsc(requestNode.get("hsc").asText());
        request.setMacAdr(requestNode.get("macAdr").asText());

        return request;
    }
}
