package es.localchat.websocket.model.event.user;

import es.localchat.websocket.model.event.AbstractUserWebSocketEvent;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor
public class UserComesOnlineEvent extends AbstractUserWebSocketEvent {
    public UserComesOnlineEvent(UUID userId) {
        super(WebSocketEventType.USER_WENT_ONLINE, userId);
    }
}
