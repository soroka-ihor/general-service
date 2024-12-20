package es.localchat.websocket.redis.subscriber;

import es.localchat.websocket.broadcasting.WebSocketPrivateChatEventBroadcaster;
import es.localchat.websocket.model.event.PrivateChatWebSocketEvent;
import es.localchat.websocket.model.event.parser.WebSocketEventParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PrivateChatEventRedisSubscriber implements MessageListener {

    @Autowired
    private WebSocketPrivateChatEventBroadcaster chatManager;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private WebSocketEventParser eventParser;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        Object deserializedMessage = redisTemplate.getValueSerializer().deserialize(message.getBody());
        var event = (PrivateChatWebSocketEvent) eventParser.parse(String.valueOf(deserializedMessage));
        chatManager.sendEventByUserId(
                event.getRecipientUserId(),
                event
        );
    }

}
