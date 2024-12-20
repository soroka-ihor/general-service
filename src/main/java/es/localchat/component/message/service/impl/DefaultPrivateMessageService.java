package es.localchat.component.message.service.impl;

import es.local.chat.sharedentities.exception.ResourceNotFoundException;
import es.local.chat.sharedentities.model.message.AbstractMessage;
import es.local.chat.sharedentities.model.message.direct.ForwardMessageType;
import es.local.chat.sharedentities.model.message.direct.PrivateMessage;
import es.local.chat.sharedentities.model.privatechat.PrivateChat;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.message.mapper.PrivateMessageToDto;
import es.localchat.component.message.model.ForwardMessageRequest;
import es.localchat.component.message.model.PrivateMessageDTO;
import es.localchat.component.message.repository.PrivateMessageRepository;
import es.localchat.component.message.service.PrivateMessageService;
import es.localchat.component.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DefaultPrivateMessageService implements PrivateMessageService {

    private final PrivateMessageRepository repository;
    private final UserService userService;

    @Override
    public void markRead(PrivateMessage message, UserEntity user) {
        if (message.getUser().getId().equals(user.getId())) {
            message.setReadAt(ZonedDateTime.now());
            repository.save(message);
        }
    }

    @Override
    public void markRead(Set<PrivateMessage> messages, UserEntity user) {
        messages.stream()
                .filter(m -> m.getReadAt() == null)
                .forEach(m -> markRead(m, user));
    }

    @Override
    public boolean markRead(PrivateMessageDTO messageDTO, UUID userId) {
        boolean successfullyRead = false;
        if (repository.findByIdAndUserId(UUID.fromString(messageDTO.getMessageId()), userId).isPresent()) {
            return successfullyRead;
        }
        var message = repository.findById(UUID.fromString(messageDTO.getMessageId()));
        if (message.isPresent()) {
            message.get().setReadAt(ZonedDateTime.now());
            repository.save(message.get());
        }
        successfullyRead = true;
        return successfullyRead;
    }

    @Override
    public boolean markRead(String messageId, UUID userId) {
        boolean successfullyRead = false;
        if (repository.findByIdAndUserId(UUID.fromString(messageId), userId).isPresent()) {
            return successfullyRead;
        }
        var message = repository.findById(UUID.fromString(messageId));
        if (message.isPresent()) {
            message.get().setReadAt(ZonedDateTime.now());
            repository.save(message.get());
        }
        successfullyRead = true;
        return successfullyRead;
    }

    @Override
    public PrivateMessage createAndSaveMessage(UserEntity creator, String text, PrivateChat chat, String replyToMessageId,
                                               ForwardMessageRequest forwardRequest) {
        var message = new PrivateMessage(
                null,
                ZonedDateTime.now(),
                text,
                creator,
                null,
                chat,
                Optional.ofNullable(replyToMessageId).map(
                        r -> repository.findById(UUID.fromString(r)).orElse(null)
                ).orElse(null),
                Optional.ofNullable(forwardRequest).map(ForwardMessageRequest::getMessageId).orElse(null),
                Optional.ofNullable(forwardRequest).map(ForwardMessageRequest::getType).orElse(null)
        );
        return repository.save(message);
    }

    @Override
    public int messagesCount(Long chatId, ZonedDateTime before) {
        return repository.countMessagesByChatId(chatId, before);
    }

    @Override
    public PrivateMessage getLastMessageFromChat(Long chatId) {
        return repository.findLastMessageInChat(chatId).orElse(null);
    }

    @Override
    public List<PrivateMessageDTO> getMessagesFromChat(Long chatId, ZonedDateTime before, Pageable page) {
        throw new RuntimeException("Not implemented yet");
    }
}
