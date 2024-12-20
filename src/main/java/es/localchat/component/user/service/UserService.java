package es.localchat.component.user.service;

import es.local.chat.sharedentities.model.user.About;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.auth.model.SignUpRequest;
import es.localchat.component.user.model.*;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Set;
import java.util.UUID;

public interface UserService {
    UserEntity getAuthenticatedUser();
    UserEntity createUser(SignUpRequest request);
    UserAuth getUserByUUID(String uuid);
    UserDetailsService userAuth();
    MyUserProfileDTO getCurrentUserProfile();
    UserProfileDTO getUserProfileById(UUID userId);
    UserProfileDTO getUserProfileByIds(UUID currentUserId, UUID userId);
    UpdateUsernameResponse updateUsername(UpdateUsernameRequest request);
    UpdateFullNameResponse updateFullName(UpdateFullNameRequest request);
    UpdateUserAboutResponse updateAbout(Set<About> about, Set<String> customAbout);
    CurrentPositionUpdateResponse updateCurrentPosition(CurrentPositionUpdateRequest request);
    CurrentPositionUpdateResponse updateCurrentPosition(CurrentPositionUpdateRequest request, UserEntity user);
    UserEntity getUserByUUID(UUID uuid);
    UserEntity getById(UUID userId);
    UserEntity updateHash(UserEntity user);
    Double distanceBetweenTwoUsers(UserEntity user1, UserEntity user2);
    void makeOnline(UserEntity user);
    void makeOffline(UserEntity user);
    String getUserHash(String userId);
    OnlineStatus onlineStatus(UUID userId);
    Double getDistanceToUser(UUID userId);
}
