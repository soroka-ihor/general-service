package es.localchat.websocket.model.event.parser.impl;

import es.localchat.utils.JacksonObjectMapperWrapper;
import es.localchat.websocket.model.event.AbstractWebSocketEvent;
import es.localchat.websocket.model.event.parser.WebSocketEventParser;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
@AllArgsConstructor
public class DefaultWebSocketEventParser implements WebSocketEventParser {

    private final JacksonObjectMapperWrapper objectMapper;

    @Override
    public AbstractWebSocketEvent parse(String json) {
        Map<String, String> jsonMap = objectMapper.readValue(json, HashMap.class);
        if (!jsonMap.containsKey("type")) {
            throw new RuntimeException("JSON object does not contain 'type' field");
        }
        var type = WebSocketEventType.valueOf(jsonMap.get("type"));
        if (type == null || type.getClassType() == null) {
            throw new RuntimeException("Not supported event type: " + jsonMap.get("type"));
        }
        return (AbstractWebSocketEvent) objectMapper.readValue(json, type.getClassType());
    }
}
