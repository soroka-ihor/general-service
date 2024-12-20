package es.localchat.websocket.model.event;

import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor
public abstract class AbstractPrivateChatWebSocketEvent extends AbstractWebSocketEvent
        implements PrivateChatWebSocketEvent {

    private Object payload;
    private UUID recipientUserId;

    public AbstractPrivateChatWebSocketEvent(WebSocketEventType type, Object payload, UUID recipientUserId) {
        super(type);
        this.payload = payload;
        this.recipientUserId = recipientUserId;
    }

    @Override
    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public Object getPayload() {
        return this.payload;
    }

    @Override
    public void setRecipientUserId(UUID recipientUserId) {
        this.recipientUserId = recipientUserId;
    }

    @Override
    public UUID getRecipientUserId() {
        return this.recipientUserId;
    }
}
