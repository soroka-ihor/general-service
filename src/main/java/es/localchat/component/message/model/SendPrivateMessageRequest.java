package es.localchat.component.message.model;

import es.localchat.validator.uuid.ValidUUID;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendPrivateMessageRequest {

    @Schema(description = "Message could be omitted in the case, when you want just to do forwarding.")
    Optional<String> message;

    @ValidUUID
    @Schema(description = "This variable could be omitted, if there's no reply.")
    Optional<String> repliedMessageId;

    @ValidUUID @Schema(description = "This variable could be omitted, " +
            "if a request is not intended to forwarding another message.")
    Optional<ForwardMessageRequest> messageForwardRequest;

}
