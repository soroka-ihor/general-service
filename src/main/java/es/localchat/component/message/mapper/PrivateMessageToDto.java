package es.localchat.component.message.mapper;

import es.local.chat.sharedentities.model.message.AbstractMessage;
import es.local.chat.sharedentities.model.message.direct.PrivateMessage;
import es.localchat.component.message.model.MessageForward;
import es.localchat.component.message.model.MessageReply;
import es.localchat.component.message.model.PrivateMessageDTO;
import java.util.Optional;
import java.util.function.Supplier;

public interface PrivateMessageToDto {
    static PrivateMessageDTO map(PrivateMessage message, Supplier<AbstractMessage> forwardedMessage) {
        return new PrivateMessageDTO(
                message.getChat().getId(),
                message.getId().toString(),
                message.getUser().getId().toString(),
                message.getText(),
                message.getCreatedAt(),
                message.getReadAt(),
                Optional.ofNullable(message.getReplyTo()).map(
                        mr -> {
                            var messageReply = new MessageReply();
                            messageReply.setMessage(mr.getText());
                            messageReply.setAuthorId(mr.getUser().getId().toString());
                            messageReply.setMessageId(mr.getId().toString());
                            messageReply.setSentAt(mr.getCreatedAt());
                            return messageReply;
                        }
                ).orElse(null),
                Optional.ofNullable(forwardedMessage.get()).map(
                        fm -> {
                            var messageForward = new MessageForward();
                            messageForward.setMessageId(fm.getId().toString());
                            messageForward.setAuthorId(fm.getUser().getId().toString());
                            messageForward.setType(message.getForwardMessageType());
                            messageForward.setSentAt(fm.getCreatedAt());
                            return messageForward;
                        }
                ).orElse(null)
        );
    }
}
