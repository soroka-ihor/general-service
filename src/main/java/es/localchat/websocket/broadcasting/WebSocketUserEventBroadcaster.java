package es.localchat.websocket.broadcasting;

import es.localchat.component.user.service.UserService;
import es.localchat.utils.JacksonObjectMapperWrapper;
import es.localchat.websocket.model.event.UserWebSocketEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@AllArgsConstructor
public class WebSocketUserEventBroadcaster {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketUserEventBroadcaster.class);
    public final Map<WebSocketSession, UUID> activeSessions = new ConcurrentHashMap<>();
    public final Map<UUID, Set<Observer>> activeObservers = new ConcurrentHashMap<>();
    private final UserService userService;
    private final JacksonObjectMapperWrapper objectMapperWrapper;

    public void subscribe(WebSocketSession session, UUID observableUserId, String observableUserHash) {
        checkHashesAndRespondWithActualProfileIfNotEqual(session, observableUserId, observableUserHash);
        var observerId = activeSessions.get(session);
        var observer = new Observer(observerId, session);
        if (!activeObservers.containsKey(observableUserId)) {
            activeObservers.put(observableUserId, new HashSet<>(Set.of(observer)));
        } else {
            activeObservers.get(observableUserId).add(observer);
        }
    }

    public void unsubscribe(WebSocketSession session, UUID observableUserId) {
        var observerId = activeSessions.get(session);
        var fakeObserver = new Observer(observerId, null);
        activeObservers.get(observableUserId).remove(fakeObserver);
    }

    public void removeObserver(WebSocketSession session) {
        var fakeObserver = new Observer(activeSessions.get(session), session);
        var keysToRemove = new ArrayList<UUID>();
        activeObservers.forEach(
                (k, v) -> {
                    v.remove(fakeObserver);
                    if (activeObservers.get(k).isEmpty()) {
                        keysToRemove.add(k);
                    }
                }
        );
        keysToRemove.forEach(
                k -> activeObservers.remove(k)
        );
    }

    public void broadcastMessageToObservers(UUID observableUserId, UserWebSocketEvent event) {
        if (activeObservers.containsKey(observableUserId)) {
            activeObservers.get(observableUserId).forEach(
                    observer -> {
                        try {
                            observer.getSession().sendMessage(new TextMessage(objectMapperWrapper.writeValue(event)));
                        } catch (IOException e) {
                            logger.error(e.getMessage());
                        }
                    }
            );
        }
    }

    public void addActiveSession(WebSocketSession session, UUID userId) {
        activeSessions.put(session, userId);
    }

    public void removeSession(WebSocketSession session) {
        activeSessions.remove(session);
    }

    private void checkHashesAndRespondWithActualProfileIfNotEqual(
            WebSocketSession session,
            UUID observableUserId,
            String observableUserHash
    ) {
        String actualUserHash = userService.getUserHash(observableUserId.toString());
        try {
            var userProfile = userService.getUserProfileByIds(activeSessions.get(session), observableUserId);
            if (actualUserHash == null || !actualUserHash.equals(observableUserHash)) {
                session.sendMessage(
                        new TextMessage(
                                objectMapperWrapper.writeValue(userProfile)
                        )
                );
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @Getter
    private static class Observer {

        private final UUID userId;
        private final WebSocketSession session;

        public Observer(UUID userId, WebSocketSession session) {
            this.userId = userId;
            this.session = session;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(userId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Observer observer = (Observer) o;
            return Objects.equals(userId, observer.userId);
        }
    }
}
