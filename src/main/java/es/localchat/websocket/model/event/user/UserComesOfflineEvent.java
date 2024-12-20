package es.localchat.websocket.model.event.user;

import es.localchat.websocket.model.event.AbstractUserWebSocketEvent;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;

import java.util.UUID;

@NoArgsConstructor
public class UserComesOfflineEvent extends AbstractUserWebSocketEvent {
    public UserComesOfflineEvent(UUID userId) {
        super(WebSocketEventType.USER_WENT_OFFLINE, userId);
    }
}
