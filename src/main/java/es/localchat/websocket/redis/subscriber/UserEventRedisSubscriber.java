package es.localchat.websocket.redis.subscriber;

import es.localchat.websocket.broadcasting.WebSocketUserEventBroadcaster;
import es.localchat.websocket.model.event.UserWebSocketEvent;
import es.localchat.websocket.model.event.parser.WebSocketEventParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserEventRedisSubscriber implements MessageListener {

    @Autowired
    WebSocketUserEventBroadcaster notificationsManager;
    @Autowired RedisTemplate<String, String> redisTemplate;
    @Autowired
    WebSocketEventParser eventParser;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        var payload = String.valueOf(redisTemplate.getValueSerializer().deserialize(message.getBody()));
        var event = (UserWebSocketEvent) eventParser.parse(payload);
        notificationsManager.broadcastMessageToObservers(event.getUserId(), event);
    }
}
