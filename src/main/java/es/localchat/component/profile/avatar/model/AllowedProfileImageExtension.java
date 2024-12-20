package es.localchat.component.profile.avatar.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum AllowedProfileImageExtension {

    JPG("jpg"), JPEG("jpeg"), GIF("gif"), PNG("png");

    String name;

    public static boolean contains(String extension) {
        for (AllowedProfileImageExtension allowedProfileImageExtension : AllowedProfileImageExtension.values()) {
            if (allowedProfileImageExtension.name.equals(extension)) {
                return true;
            }
        }
        return false;
    }
}
