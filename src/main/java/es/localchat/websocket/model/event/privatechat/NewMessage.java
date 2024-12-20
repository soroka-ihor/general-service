package es.localchat.websocket.model.event.privatechat;


import es.localchat.component.message.model.PrivateMessageDTO;
import es.localchat.websocket.model.event.AbstractPrivateChatWebSocketEvent;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.NoArgsConstructor;
import java.util.UUID;

@NoArgsConstructor
public class NewMessage extends AbstractPrivateChatWebSocketEvent {
    public NewMessage(PrivateMessageDTO payload, UUID recipientUserId) {
        super(WebSocketEventType.PRIVATE_CHAT_NEW_MESSAGE, payload, recipientUserId);
    }
}
