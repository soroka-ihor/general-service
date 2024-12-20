package es.localchat.websocket.redis.subscriber;

import es.localchat.utils.JacksonObjectMapperWrapper;
import es.localchat.websocket.broadcasting.WebSocketPrivateChatCustomConnectionManager;
import es.localchat.websocket.redis.model.CustomEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class PrivateChatCustomEventSubscriber implements MessageListener {

    @Autowired
    private JacksonObjectMapperWrapper wrapper;

    @Autowired
    private WebSocketPrivateChatCustomConnectionManager connectionManager;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        var event = wrapper.readValue(message.toString(), CustomEvent.class);
        connectionManager.sendMessage(UUID.fromString(event.getUserId()), wrapper.writeValue(event));
    }
}
