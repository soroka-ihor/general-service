package es.localchat.component.user.model;

import es.local.chat.sharedentities.model.customabout.CustomUserAbout;
import es.local.chat.sharedentities.model.user.About;
import es.localchat.component.profile.avatar.model.ProfileImageDto;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class UserProfileDTO {
    String id;
    String fullName;
    String username;
    List<ProfileImageDto> gallery;
    Set<About> aboutMe;
    List<CustomUserAbout> customAboutMe;
    String userHash;
}
