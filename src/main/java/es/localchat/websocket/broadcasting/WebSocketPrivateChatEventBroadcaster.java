package es.localchat.websocket.broadcasting;

import es.localchat.utils.JacksonObjectMapperWrapper;
import es.localchat.websocket.model.event.PrivateChatWebSocketEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class WebSocketPrivateChatEventBroadcaster {

    private final Map<UUID, WebSocketSession> activeSessions = new ConcurrentHashMap<>();
    private final Logger logger = LoggerFactory.getLogger(WebSocketPrivateChatEventBroadcaster.class);

    @Autowired
    private JacksonObjectMapperWrapper objectMapper;

    public void storeSession(WebSocketSession wsSession, UUID userId) {
        activeSessions.put(userId, wsSession);
    }

    public void closeSession(WebSocketSession session) {
        try {
            UUID keyToRemove = null;
            for (Map.Entry<UUID, WebSocketSession> entry : activeSessions.entrySet()) {
                if (entry.getValue().getId().equals(session.getId())) {
                    keyToRemove = entry.getKey();
                    break;
                }
            }
            if (keyToRemove != null) {
                activeSessions.remove(keyToRemove);
            }
            session.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void sendEventByUserId(UUID userId, PrivateChatWebSocketEvent event) {
        if (activeSessions.containsKey(userId)) {
            try {
                activeSessions.get(userId).sendMessage(new TextMessage(objectMapper.writeValue(event)));
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }

    public UUID getUserIdFromSession(WebSocketSession session) {
        return activeSessions.entrySet()
                .stream()
                .filter(entry -> entry.getValue().equals(session))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
    }
}
