package es.localchat.websocket.redis.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RedisStreamRecord {
    private String eventId;
    private CustomEvent event;
}