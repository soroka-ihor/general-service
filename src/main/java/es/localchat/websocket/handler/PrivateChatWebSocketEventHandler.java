package es.localchat.websocket.handler;

import es.localchat.component.auth.service.JwtService;
import es.localchat.component.message.service.PrivateMessageService;
import es.localchat.component.user.service.UserService;
import es.localchat.websocket.broadcasting.WebSocketPrivateChatEventBroadcaster;
import es.localchat.websocket.model.event.PrivateChatWebSocketEvent;
import es.localchat.websocket.model.event.parser.WebSocketEventParser;
import es.localchat.websocket.model.event.type.WebSocketEventType;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivateChatWebSocketEventHandler extends TextWebSocketHandler {

    @Autowired
    JwtService jwtService;
    @Autowired
    WebSocketPrivateChatEventBroadcaster broadcaster;
    @Autowired
    UserService userService;
    @Autowired
    WebSocketEventParser eventParser;
    @Autowired
    PrivateMessageService messageService;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // TODO: Implement
//        var payload = String.valueOf(message.getPayload());
//        var event = (PrivateChatWebSocketEvent) eventParser.parse(payload);
//        if (event != null && event.getType() == WebSocketEventType.PRIVATE_CHAT_READ_MESSAGE) {
//            var messageId = String.valueOf(event.getPayload());
//            var successfullyRead = messageService
//                    .markRead(messageId, broadcaster.getUserIdFromSession(session) /*currentUserId*/);
//            if (successfullyRead) {
//                broadcaster.sendEventByUserId(event.getRecipientUserId(), event);
//            }
//        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userUUID = jwtService.extractUuid(
                session.getHandshakeHeaders().get("Authorization").get(0).split(" ")[1]
        );
        broadcaster.storeSession(session, userService.getUserByUUID(
                UUID.fromString(userUUID)
        ).getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        broadcaster.closeSession(session);
    }

}
