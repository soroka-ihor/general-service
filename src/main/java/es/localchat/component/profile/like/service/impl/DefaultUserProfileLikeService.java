package es.localchat.component.profile.like.service.impl;

import es.local.chat.sharedentities.exception.ResourceNotFoundException;
import es.local.chat.sharedentities.model.user.UserEntity;
import es.local.chat.sharedentities.model.user.UserProfileLike;
import es.localchat.component.profile.like.model.UserProfileLikeDTO;
import es.localchat.component.profile.like.model.UserProfileLikeResponse;
import es.localchat.component.profile.like.repository.UserProfileLikeRepository;
import es.localchat.component.profile.like.service.UserProfileLikeService;
import es.localchat.component.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@AllArgsConstructor
public class DefaultUserProfileLikeService implements UserProfileLikeService {

    private final UserService userService;
    private final UserProfileLikeRepository repository;

    @Override
    public UserProfileLikeResponse likeProfile(UUID likedUserId) {
        var currentUser = userService.getAuthenticatedUser();
        var likedUser = Optional.ofNullable(userService.getById(likedUserId)).orElseThrow(
                () -> new ResourceNotFoundException("User with id " + likedUserId + " not found.")
        );
        var profileLike = repository.findByUserAndLikedUser(currentUser, likedUser);
        var like = new UserProfileLikeResponse(currentUser.getId(), likedUserId, false, ZonedDateTime.now());
        if (profileLike.isPresent()) {
            repository.delete(profileLike.get());
            return like;
        }
        like.setLiked(true);
        repository.save(new UserProfileLike(null, currentUser, likedUser, ZonedDateTime.now()));
        return like;
    }

    @Override
    public List<UserProfileLikeDTO> getMyLikes() {
        var currentUser = userService.getAuthenticatedUser();
        return repository.findAllByUser(currentUser.getId());
    }

    @Override
    public List<UserProfileLikeDTO> getUsersWhoLikeMyProfile() {
        var currentUser = userService.getAuthenticatedUser();
        return repository.findAllByLikedUser(currentUser.getId());
    }

    @Override
    public boolean userLikesMe(UserEntity currentUser, UserEntity userWhoMightLikeMe) {
        return repository.findByUserAndLikedUser(userWhoMightLikeMe, currentUser).isPresent();
    }

    @Override
    public List<UserProfileLikeDTO> getUserLikes(UserEntity requestedUser) {
        throw new RuntimeException("Not implemented yet");
    }
}
