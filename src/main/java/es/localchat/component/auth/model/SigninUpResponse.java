package es.localchat.component.auth.model;


import es.localchat.component.user.model.MyUserProfileDTO;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SigninUpResponse {
    String JWT;
    String userUUID;
    MyUserProfileDTO userProfile;
}
