package es.localchat.websocket.service;

import org.springframework.web.socket.WebSocketSession;

public interface OfflineEventProcessor {

    void checkOfflineEvents(String userId, WebSocketSession session);
    void acknowledgeEvent(String userId, String eventId);

}
