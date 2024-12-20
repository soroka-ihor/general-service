package es.localchat.websocket.redis.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisMessagePublisher {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public void publish(String message, String channel) {
        redisTemplate.convertAndSend(channel, message);
    }

}
