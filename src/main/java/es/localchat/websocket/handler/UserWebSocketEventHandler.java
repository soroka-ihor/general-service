package es.localchat.websocket.handler;

import es.local.chat.sharedentities.model.user.UserEntity;

import es.localchat.component.auth.service.JwtService;
import es.localchat.component.user.service.UserService;
import es.localchat.constant.PubSubTopic;
import es.localchat.utils.JacksonObjectMapperWrapper;
import es.localchat.websocket.broadcasting.WebSocketUserEventBroadcaster;
import es.localchat.websocket.model.event.UserWebSocketEvent;
import es.localchat.websocket.model.event.parser.WebSocketEventParser;
import es.localchat.websocket.model.event.user.RegisterObserverEvent;
import es.localchat.websocket.model.event.user.UserComesOfflineEvent;
import es.localchat.websocket.model.event.user.UserComesOnlineEvent;
import es.localchat.websocket.redis.publisher.RedisMessagePublisher;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.UUID;

import static es.localchat.websocket.model.event.type.WebSocketEventType.REGISTER_OBSERVER;
import static es.localchat.websocket.model.event.type.WebSocketEventType.UNREGISTER_OBSERVER;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserWebSocketEventHandler extends TextWebSocketHandler {

    private static final Logger logger = LoggerFactory.getLogger(UserWebSocketEventHandler.class);

    @Autowired
    JwtService jwtService;
    @Autowired
    UserService userService;
    @Autowired
    WebSocketUserEventBroadcaster notificationsManager;
    @Autowired
    RedisMessagePublisher messagePublisher;
    @Autowired
    JacksonObjectMapperWrapper objectMapper;
    @Autowired
    WebSocketEventParser eventParser;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userUUID = jwtService.extractUuid(
                session.getHandshakeHeaders().get("Authorization").get(0).split(" ")[1]
        );
        UserEntity currentUser = userService.getUserByUUID(UUID.fromString(userUUID));
        userService.makeOnline(currentUser);
        var event = new UserComesOnlineEvent(currentUser.getId());
        messagePublisher.publish(
               objectMapper.writeValue(event),
               PubSubTopic.PRIVATE_NOTIFICATIONS_TOPIC_NAME
        );
        notificationsManager.addActiveSession(session, currentUser.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var event = new UserComesOfflineEvent(notificationsManager.activeSessions.get(session));
        try {
            messagePublisher.publish(
                    objectMapper.writeValue(event),
                    PubSubTopic.PRIVATE_NOTIFICATIONS_TOPIC_NAME
            );
            session.close();
            var currentUser = userService.getById(
                    notificationsManager.activeSessions.get(session)
            );
            userService.makeOffline(currentUser);
            notificationsManager.removeObserver(session);
            notificationsManager.removeSession(session);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = String.valueOf(message.getPayload());
        var event = (UserWebSocketEvent) eventParser.parse(payload);
        if (event.getType() == REGISTER_OBSERVER) {
            notificationsManager.subscribe(session, event.getUserId(), ((RegisterObserverEvent) event).getUserHash());
        } else if (event.getType() == UNREGISTER_OBSERVER) {
            notificationsManager.unsubscribe(session, event.getUserId());
        } else {
            messagePublisher.publish((String) message.getPayload(), PubSubTopic.PRIVATE_NOTIFICATIONS_TOPIC_NAME);
        }
    }
}
