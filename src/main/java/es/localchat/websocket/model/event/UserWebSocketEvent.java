package es.localchat.websocket.model.event;

import es.localchat.websocket.model.event.type.WebSocketEventType;
import java.util.UUID;

public interface UserWebSocketEvent {
    UUID getUserId();
    void setUserId(UUID userId);
    WebSocketEventType getType();
}
