package es.localchat.component.profile.like.service;

import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.profile.like.model.UserProfileLikeDTO;
import es.localchat.component.profile.like.model.UserProfileLikeResponse;

import java.util.List;
import java.util.UUID;

public interface UserProfileLikeService {
    UserProfileLikeResponse likeProfile(UUID likedUserId);
    List<UserProfileLikeDTO> getMyLikes();
    List<UserProfileLikeDTO> getUserLikes(UserEntity requestedUser);
    List<UserProfileLikeDTO> getUsersWhoLikeMyProfile();
    boolean userLikesMe(UserEntity currentUser, UserEntity userWhoMightLikeCurrentUser);
}
