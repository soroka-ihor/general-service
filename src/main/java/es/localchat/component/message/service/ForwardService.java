package es.localchat.component.message.service;

import es.local.chat.sharedentities.model.message.AbstractMessage;
import es.local.chat.sharedentities.model.message.direct.ForwardMessageType;

import java.util.UUID;

public interface ForwardService {
    AbstractMessage findForwardedMessage(UUID messageId, ForwardMessageType messageType);
}
