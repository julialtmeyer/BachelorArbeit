package de.htwsaar.verwaltung_service.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.htwsaar.verwaltung_service.mqtt.messages.Heartbeat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class HeartbeatDeserializer extends StdDeserializer<Heartbeat> {

        private final Logger logger = LoggerFactory.getLogger(HeartbeatDeserializer.class);

public HeartbeatDeserializer(){
        this(null);
        }

public HeartbeatDeserializer(Class<?> vc){
        super(vc);
        }

@Override
public Heartbeat deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        JsonNode jsonNode = p.getCodec().readTree(p);
        JsonNode heartbeatNode = jsonNode.get("heartbeat");
        Heartbeat heartbeat = new Heartbeat();
        try {
                heartbeat.setId(heartbeatNode.get("Id").asLong());
                heartbeat.setName(heartbeatNode.get("robotName").asText());
                heartbeat.setHsc(heartbeatNode.get("hsc").asText());
                heartbeat.setMacAdr(heartbeatNode.get("macAdr").asText());
        }
        catch (NullPointerException e){
                logger.error("Error while reading Request! Payload does not match the requirements. {}", jsonNode, e);
        }


        return heartbeat;
        }
}
