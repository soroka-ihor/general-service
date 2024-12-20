package es.localchat.component.user.model;

import es.localchat.validator.username.ValidUsername;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Optional;

@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUsernameRequest {
    @ValidUsername
    Optional<String> username;
}
