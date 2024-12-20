package es.localchat.component.message.model;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageReply {
    String messageId;
    String authorId;
    String message;
    ZonedDateTime sentAt;
}