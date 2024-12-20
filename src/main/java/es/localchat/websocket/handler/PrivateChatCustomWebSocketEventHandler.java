package es.localchat.websocket.handler;


import es.localchat.component.auth.service.JwtService;
import es.localchat.component.user.service.UserService;
import es.localchat.utils.JacksonObjectMapperWrapper;
import es.localchat.websocket.broadcasting.WebSocketPrivateChatCustomConnectionManager;
import es.localchat.websocket.service.OfflineEventProcessor;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class PrivateChatCustomWebSocketEventHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(PrivateChatCustomWebSocketEventHandler.class);

    @Autowired
    JwtService jwtService;
    @Autowired
    WebSocketPrivateChatCustomConnectionManager connectionManager;
    @Autowired
    UserService userService;
    @Autowired
    OfflineEventProcessor offlineEventProcessor;
    @Autowired
    JacksonObjectMapperWrapper wrapper;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userUUID = jwtService.extractUuid(
                session.getHandshakeHeaders().get("Authorization").get(0).split(" ")[1]
        );
        var currentUser = userService.getUserByUUID(UUID.fromString(userUUID));
        connectionManager.addConnection(
                currentUser.getId().toString(),
                session
        );
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        try {
            ArrayList<LinkedHashMap> eventsToAcknowledge = (ArrayList<LinkedHashMap>) wrapper.readValue(
                    String.valueOf(message.getPayload()),
                    Object.class
            );
            eventsToAcknowledge.forEach(
                    event ->
                            offlineEventProcessor.acknowledgeEvent(
                                    connectionManager.getUserId(session),
                                    event.get("eventId").toString()
                            )
            );
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        connectionManager.removeConnection(session.getId());
    }
}
