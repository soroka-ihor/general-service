package es.localchat.websocket.model.event.parser;

import es.localchat.websocket.model.event.AbstractWebSocketEvent;

public interface WebSocketEventParser {
    AbstractWebSocketEvent parse(String json);
}
