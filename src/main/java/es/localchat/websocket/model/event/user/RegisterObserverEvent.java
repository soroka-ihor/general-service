package es.localchat.websocket.model.event.user;


import es.localchat.websocket.model.event.AbstractUserWebSocketEvent;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class RegisterObserverEvent extends AbstractUserWebSocketEvent {

    private String userHash;

    public RegisterObserverEvent(UUID userId, String userHash) {
        super(WebSocketEventType.REGISTER_OBSERVER, userId);
        this.userHash = userHash;
    }
    public String getUserHash() {
        return userHash;
    }
}
