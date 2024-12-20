package es.localchat.component.message.model;

import es.local.chat.sharedentities.model.message.direct.ForwardMessageType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ForwardMessageRequest {

    private ForwardMessageType type;
    private UUID messageId;

}

