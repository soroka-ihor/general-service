package es.localchat.component.user.model;

import es.local.chat.sharedentities.model.user.About;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UpdateUserAboutResponse {
    String userId;
    Set<About> about;
}
