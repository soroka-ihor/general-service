package es.localchat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true)
public enum ApiPaths {

    V1("v1"),

    ;

    public static final String V1_MAPPING = "/api/v1";
    public static final String USER_NOTIFICATIONS = "/user";
    public static final String AUTH_MAPPING = "/auth";
    public static final String AVATAR_MAPPING = "/avatar";
    public static final String USER_MAPPING = "/user";
    public static final String PROFILE_LIKE_MAPPING = "/profilelike";
    // chat section
    public static final String CHAT_GLOBAL = "/chat/local";
    public static final String CHAT_PRIVATE = "/chat/private";
    public static final String CHAT_LOCAL_LIKE = "/chat/local/like";

    String version;
}
