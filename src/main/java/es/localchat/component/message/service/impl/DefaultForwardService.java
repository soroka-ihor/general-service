package es.localchat.component.message.service.impl;

import es.local.chat.sharedentities.model.message.AbstractMessage;
import es.local.chat.sharedentities.model.message.direct.ForwardMessageType;
import es.localchat.component.message.service.ForwardService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class DefaultForwardService implements ForwardService {

    @Override
    public AbstractMessage findForwardedMessage(UUID messageId, ForwardMessageType messageType) {
        return null;
    }
}
