package es.localchat.websocket.service.impl;

import es.localchat.utils.JacksonObjectMapperWrapper;
import es.localchat.websocket.redis.model.CustomEvent;
import es.localchat.websocket.redis.model.RedisStreamRecord;
import es.localchat.websocket.service.OfflineEventProcessor;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class RedisOfflineEventProcessor implements OfflineEventProcessor {

    private final RedisTemplate<String, String> redisTemplate;
    private final JacksonObjectMapperWrapper jacksonObjectMapperWrapper;
    private final Logger logger = LoggerFactory.getLogger(RedisOfflineEventProcessor.class);
    private final String OFFLINE_EVENT_INBOX_NAME = "offline-event-inbox";

    @Override
    public void checkOfflineEvents(String userId, WebSocketSession session) {
        String streamKey = OFFLINE_EVENT_INBOX_NAME + ":" + userId;

        // Define the ReadOffset: Start from the last seen event ID or the beginning ("0")
        ReadOffset readOffset = ReadOffset.from("0");

        // Use StreamOffset to specify the stream and offset
        StreamOffset<String> streamOffset = StreamOffset.create(streamKey, readOffset);

        // Read events from the stream
        List<MapRecord<String, Object, Object>> records = redisTemplate
                .opsForStream()
                .read(StreamReadOptions.empty().count(10), streamOffset);

        // Convert the records to a list of events
        List<RedisStreamRecord> events = new ArrayList<>();
        if (records != null) {
            for (MapRecord<String, Object, Object> record : records) {
                var redisEventJson = (String) record.getValue().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toList()).get(0);
                var redisEvent = jacksonObjectMapperWrapper.readValue(redisEventJson, CustomEvent.class);
                var streamEvent = new RedisStreamRecord();
                streamEvent.setEventId(record.getId().getValue());
                streamEvent.setEvent(redisEvent);
                events.add(streamEvent);
            }
        }

        events.forEach(
                e -> {
                    try {
                        session.sendMessage(new TextMessage(
                                jacksonObjectMapperWrapper.writeValue(e)
                        ));
                    } catch (IOException ex) {
                        logger.error(ex.getMessage());
                    }
                }
        );

    }

    @Override
    public void acknowledgeEvent(String userId, String eventId) {
        try {
            String streamKey = OFFLINE_EVENT_INBOX_NAME + ":" + userId;
            redisTemplate.opsForStream().delete(streamKey, eventId);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
