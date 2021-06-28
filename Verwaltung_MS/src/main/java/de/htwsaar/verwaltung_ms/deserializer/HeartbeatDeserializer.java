package de.htwsaar.verwaltung_ms.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import de.htwsaar.verwaltung_ms.mqtt.messages.Heartbeat;
import org.springframework.boot.context.event.SpringApplicationEvent;

import java.io.IOException;

public class HeartbeatDeserializer extends StdDeserializer<Heartbeat> {

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
        heartbeat.setId(heartbeatNode.get("Id").asLong());
        heartbeat.setName(heartbeatNode.get("robotName").asText());
        heartbeat.setHsc(heartbeatNode.get("hsc").asText());
        heartbeat.setMacAdr(heartbeatNode.get("macAdr").asText());

        return heartbeat;
        }
}
