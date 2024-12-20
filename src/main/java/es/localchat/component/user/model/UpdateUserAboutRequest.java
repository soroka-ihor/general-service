package es.localchat.component.user.model;

import es.local.chat.sharedentities.model.user.About;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUserAboutRequest {
    Set<About> about;
    Set<String> customAbout;
}
