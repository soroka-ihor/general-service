package es.localchat.websocket.model.event.privatechat;

import es.localchat.websocket.model.event.AbstractPrivateChatWebSocketEvent;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor
public class ReadMessage extends AbstractPrivateChatWebSocketEvent {
    public ReadMessage(String messageId, UUID recipientUserId) {
        super(WebSocketEventType.PRIVATE_CHAT_READ_MESSAGE, messageId, recipientUserId);
    }
}
