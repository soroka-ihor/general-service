package es.localchat.component.user.model;

import es.local.chat.sharedentities.model.appsetting.ClientAppSettingType;
import es.local.chat.sharedentities.model.customabout.CustomUserAbout;
import es.local.chat.sharedentities.model.user.About;
import es.localchat.component.profile.avatar.model.ProfileImageDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MyUserProfileDTO {
    String userId;
    String name;
    String username;
    double latitude;
    double longitude;
    Set<About> about;
    List<CustomUserAbout> customAbout;
    List<ProfileImageDto> gallery;
    Map<ClientAppSettingType, Object> currentSettings;
}
