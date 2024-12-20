package es.localchat.websocket.broadcasting;

import es.localchat.websocket.service.OfflineEventProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketPrivateChatCustomConnectionManager {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private OfflineEventProcessor eventProcessor;

    private final Logger logger = LoggerFactory.getLogger(WebSocketPrivateChatCustomConnectionManager.class);
    private final Map<String, String> sessionIdToUserId = new ConcurrentHashMap<>();
    private final Map<UUID, WebSocketSession> userIdToSession = new ConcurrentHashMap<>();
    public static final String ACTIVE_PRIVATE_CHAT_USERS_LIST = "active_private_chat_users";

    @Value("${spring.application.name}")
    private String applicationName;


    public void addConnection(String userId, WebSocketSession session) {
        sessionIdToUserId.put(session.getId(), userId);
        userIdToSession.put(UUID.fromString(userId), session);
        redisTemplate.opsForHash().put(ACTIVE_PRIVATE_CHAT_USERS_LIST, userId, applicationName);
        eventProcessor.checkOfflineEvents(userId, session);
    }

    public void removeConnection(String sessionId) {
        String userId = sessionIdToUserId.remove(sessionId);
        if (userId != null) {
            userIdToSession.remove(UUID.fromString(userId));
            redisTemplate.opsForHash().delete(ACTIVE_PRIVATE_CHAT_USERS_LIST, userId);
        }
    }

    public void sendMessage(UUID userId, String message) {
        try {
            userIdToSession.get(userId).sendMessage(new TextMessage(message));
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public String getUserId(WebSocketSession session) {
        return sessionIdToUserId.get(session.getId());
    }
}
