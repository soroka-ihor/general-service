package es.localchat.component.message.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrivateMessageDTO {
    Long chatId;
    String messageId;
    String authorId;
    String message;
    ZonedDateTime sentAt;
    ZonedDateTime readAt;
    MessageReply replyTo;
    MessageForward messageForward;
}
