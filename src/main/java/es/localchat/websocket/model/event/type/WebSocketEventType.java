package es.localchat.websocket.model.event.type;

import es.localchat.websocket.model.event.privatechat.NewMessage;
import es.localchat.websocket.model.event.privatechat.ReadMessage;
import es.localchat.websocket.model.event.user.RegisterObserverEvent;
import es.localchat.websocket.model.event.user.UnregisterObserverEvent;
import es.localchat.websocket.model.event.user.UserComesOfflineEvent;
import es.localchat.websocket.model.event.user.UserComesOnlineEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebSocketEventType {

    GLOBAL_CHAT_UPDATE_MESSAGE(null),
    GLOBAL_CHAT_DELETE_MESSAGE(null),
    USER_PROFILE_LIKE(null),
    PRIVATE_CHAT_NEW_MESSAGE(NewMessage.class),
    PRIVATE_CHAT_READ_MESSAGE(ReadMessage.class),
    USER_WENT_ONLINE(UserComesOnlineEvent.class),
    USER_WENT_OFFLINE(UserComesOfflineEvent.class),
    REGISTER_OBSERVER(RegisterObserverEvent.class),
    UNREGISTER_OBSERVER(UnregisterObserverEvent.class),
    ;

    Class<?> classType;
}
