package es.localchat.websocket.model.mapper;

import es.localchat.component.message.model.PrivateMessageDTO;
import es.localchat.websocket.model.event.privatechat.NewMessage;
import java.util.UUID;

public interface PrivateChatMessageToEventMapper {
    static NewMessage map(UUID recipientUserId, PrivateMessageDTO payload) {
        return new NewMessage(payload, recipientUserId);
    }
}
