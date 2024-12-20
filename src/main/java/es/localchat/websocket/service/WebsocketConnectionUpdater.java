package es.localchat.websocket.service;

import java.util.UUID;

public interface WebsocketConnectionUpdater {
    void updateConnectionLocation(UUID userId, double latitude, double longitude);
    void updateObservableRadius(UUID userId, int radius);
}
