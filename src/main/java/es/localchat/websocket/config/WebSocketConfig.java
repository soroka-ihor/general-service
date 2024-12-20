package es.localchat.websocket.config;

import es.localchat.constant.ApiPaths;
import es.localchat.websocket.handler.PrivateChatCustomWebSocketEventHandler;
import es.localchat.websocket.handler.PrivateChatWebSocketEventHandler;
import es.localchat.websocket.handler.UserWebSocketEventHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@AllArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(
                privateChatWebsocketHandler(), "/ws" + ApiPaths.V1_MAPPING + ApiPaths.CHAT_PRIVATE
        ).addHandler(
                privateNotificationsWebsocketHandler(), "/ws" + ApiPaths.V1_MAPPING + ApiPaths.USER_NOTIFICATIONS
        ).addHandler(
                privateChatWebSocketEventHandlerV2(), "/ws" + ApiPaths.V1_MAPPING + "/event" + "/custom"
        ).setAllowedOrigins("*");
    }

    @Bean
    public PrivateChatCustomWebSocketEventHandler privateChatWebSocketEventHandlerV2() {
        return new PrivateChatCustomWebSocketEventHandler();
    }

    @Bean
    PrivateChatWebSocketEventHandler privateChatWebsocketHandler() {
        return new PrivateChatWebSocketEventHandler();
    }

    @Bean
    UserWebSocketEventHandler privateNotificationsWebsocketHandler() {
        return new UserWebSocketEventHandler();
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory();
    }
}
