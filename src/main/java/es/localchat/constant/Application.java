package es.localchat.constant;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Order(1)
public class Application {
    public static final String APPLICATION_ID = UUID.randomUUID().toString();
}
