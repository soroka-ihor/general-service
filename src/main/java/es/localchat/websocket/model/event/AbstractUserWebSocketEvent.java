package es.localchat.websocket.model.event;

import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor
public abstract class AbstractUserWebSocketEvent extends AbstractWebSocketEvent implements UserWebSocketEvent {

    private UUID userId;

    public AbstractUserWebSocketEvent(WebSocketEventType type, UUID userId) {
        super(type);
        this.userId = userId;
    }

    @Override
    public UUID getUserId() {
        return this.userId;
    }

    @Override
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
