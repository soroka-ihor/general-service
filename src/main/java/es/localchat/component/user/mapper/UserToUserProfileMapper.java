package es.localchat.component.user.mapper;

import es.local.chat.sharedentities.model.appsetting.ClientAppSettingType;
import es.local.chat.sharedentities.model.customabout.CustomUserAbout;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.profile.avatar.model.ProfileImageDto;
import es.localchat.component.user.model.MyUserProfileDTO;
import es.localchat.component.user.model.UserProfileDTO;
import es.localchat.constant.ApiPaths;


import java.util.*;

public interface UserToUserProfileMapper {

    static MyUserProfileDTO map(UserEntity user,
                                Map<ClientAppSettingType, Object> currentSettings,
                                List<CustomUserAbout> customUserAbouts
    ) {
        List<ProfileImageDto> gallery = new ArrayList<>();
        if (user.getProfileImages() != null) {
            gallery = user.getProfileImages().stream()
                    .map(a ->
                            new ProfileImageDto(
                                    a.getSequence(),
                                    ApiPaths.V1_MAPPING + "/avatar/" + a.getId()
                            )
                    )
                    .toList();
        }
        return MyUserProfileDTO.builder()
                .userId(user.getId().toString())
                .about(Optional.ofNullable(user.getAbout()).orElse(Collections.EMPTY_SET))
                .customAbout(Optional.ofNullable(customUserAbouts).orElse(Collections.EMPTY_LIST))
                .name(user.getFullName())
                .latitude(user.getCurrentPoint().getY())
                .longitude(user.getCurrentPoint().getX())
                .username(user.getUsername())
                .gallery(gallery)
                .currentSettings(currentSettings)
                .build();
    }

    static UserProfileDTO mapToDto(UserEntity user,
                                   Map<ClientAppSettingType, Object> currentSettings,
                                   List<CustomUserAbout> customUserAbouts
    ) {
        List<ProfileImageDto> gallery = new ArrayList<>();
        if (user.getProfileImages() != null) {
            gallery = user.getProfileImages().stream()
                    .map(a ->
                            new ProfileImageDto(
                                    a.getSequence(),
                                    ApiPaths.V1_MAPPING + "/avatar/" + a.getId()
                            )
                    )
                    .toList();
        }
        return new UserProfileDTO(
                user.getId().toString(),
                user.getFullName(),
                user.getUsername(),
                gallery,
                Optional.ofNullable(user.getAbout()).orElse(Collections.EMPTY_SET),
                Optional.ofNullable(customUserAbouts).orElse(Collections.EMPTY_LIST),
                user.getHash()
        );
    }
}
