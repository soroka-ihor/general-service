package es.localchat.component.message.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendPrivateMessageResponse {
    private String chatId;
    private String message;
}
