package es.localchat.websocket.model.event.user;

import es.localchat.websocket.model.event.AbstractUserWebSocketEvent;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class UnregisterObserverEvent extends AbstractUserWebSocketEvent {
    public UnregisterObserverEvent(UUID userId) {
        super(WebSocketEventType.UNREGISTER_OBSERVER, userId);
    }
}
