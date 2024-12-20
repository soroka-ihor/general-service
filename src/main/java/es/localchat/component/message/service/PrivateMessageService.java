package es.localchat.component.message.service;

import es.local.chat.sharedentities.model.message.direct.PrivateMessage;
import es.local.chat.sharedentities.model.privatechat.PrivateChat;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.message.model.ForwardMessageRequest;
import es.localchat.component.message.model.PrivateMessageDTO;
import org.springframework.data.domain.Pageable;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface PrivateMessageService {

    /**
     *
     * @param privateMessageId
     */
    void markRead(PrivateMessage message, UserEntity user);

    void markRead(Set<PrivateMessage> messages, UserEntity user);

    boolean markRead(PrivateMessageDTO message, UUID userId);

    boolean markRead(String messageId, UUID userId);

    PrivateMessage createAndSaveMessage(UserEntity creator, String message, PrivateChat chat, String replyToMessageId,
                                        ForwardMessageRequest forwardRequest);

    List<PrivateMessageDTO> getMessagesFromChat(Long chatId, ZonedDateTime before, Pageable page);

    int messagesCount(Long chatId, ZonedDateTime before);

    PrivateMessage getLastMessageFromChat(Long chatId);
}
