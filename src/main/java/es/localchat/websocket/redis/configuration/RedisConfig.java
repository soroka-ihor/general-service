package es.localchat.websocket.redis.configuration;

import es.localchat.websocket.redis.subscriber.PrivateChatCustomEventSubscriber;
import es.localchat.websocket.redis.subscriber.PrivateChatEventRedisSubscriber;
import es.localchat.websocket.redis.subscriber.UserEventRedisSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import static es.localchat.constant.PubSubTopic.PRIVATE_CHAT_TOPIC_NAME;
import static es.localchat.constant.PubSubTopic.PRIVATE_NOTIFICATIONS_TOPIC_NAME;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.application.name}")
    private String applicationName;

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Autowired PrivateChatEventRedisSubscriber privateChatEventRedisSubscriber,
            @Autowired PrivateChatCustomEventSubscriber privateChatV2EventRedisSubscriber,
            @Autowired UserEventRedisSubscriber privateNotificationsMessageSubscriber)
    {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory());

        container.addMessageListener(
                privateChatMessageListenerSubscriber(privateChatEventRedisSubscriber),
                new PatternTopic(PRIVATE_CHAT_TOPIC_NAME)
        );
        container.addMessageListener(
                privateNotificationsMessageListenerSubscriber(privateNotificationsMessageSubscriber),
                new PatternTopic(PRIVATE_NOTIFICATIONS_TOPIC_NAME)
        );
        container.addMessageListener(
                privateChatV2EventRedisSubscriber,
                new PatternTopic(applicationName + ":" + PRIVATE_CHAT_TOPIC_NAME)
        );
        return container;
    }


    @Bean
    public MessageListenerAdapter privateChatMessageListenerSubscriber(PrivateChatEventRedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    public MessageListenerAdapter privateNotificationsMessageListenerSubscriber(UserEventRedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    public MessageListenerAdapter privateChatV2MessageListenerSubscriber(PrivateChatCustomEventSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber);
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

}
