package es.localchat.component.message.model;

import es.local.chat.sharedentities.model.message.direct.ForwardMessageType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageForward {
    String messageId;
    String authorId;
    String message;
    ZonedDateTime sentAt;
    ForwardMessageType type;
}
