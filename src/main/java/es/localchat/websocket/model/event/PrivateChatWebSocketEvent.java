package es.localchat.websocket.model.event;

import es.localchat.websocket.model.event.type.WebSocketEventType;

import java.util.UUID;

public interface PrivateChatWebSocketEvent {
    void setPayload(Object payload);
    Object getPayload();
    void setRecipientUserId(UUID recipientUserId);
    UUID getRecipientUserId();
    WebSocketEventType getType();
}
