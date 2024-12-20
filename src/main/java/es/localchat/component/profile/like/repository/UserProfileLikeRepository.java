package es.localchat.component.profile.like.repository;

import es.local.chat.sharedentities.model.user.UserEntity;
import es.local.chat.sharedentities.model.user.UserProfileLike;
import es.localchat.component.profile.like.model.UserProfileLikeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileLikeRepository extends JpaRepository<UserProfileLike, UUID> {
    Optional<UserProfileLike> findByUserAndLikedUser(UserEntity user, UserEntity likedUser);

    @Query(
            """
                    SELECT new es.localchat.component.profile.like.model.UserProfileLikeDTO(
                        likedUser.id, 
                        likedUser.fullName, 
                        upl.created, 
                        likedUserAvatar.id
                    )
                    FROM UserProfileLike upl
                    JOIN UserEntity myUser ON upl.user.id = myUser.id
                    JOIN UserEntity likedUser ON upl.likedUser.id = likedUser.id
                    LEFT JOIN ProfileImageEntity likedUserAvatar ON likedUser.id = likedUserAvatar.user.id AND likedUserAvatar.sequence = 1
                    WHERE upl.user.id = :userId        
            """
    )
    List<UserProfileLikeDTO> findAllByUser(UUID userId);

    @Query(
            """
                    SELECT new es.localchat.component.profile.like.model.UserProfileLikeDTO(
                        whoLikeUser.id, 
                        whoLikeUser.fullName, 
                        upl.created, 
                        whoLikeUserAvatar.id
                    )
                    FROM UserProfileLike upl
                    JOIN UserEntity whoLikeUser ON upl.user.id = whoLikeUser.id
                    JOIN UserEntity likedUser ON upl.likedUser.id = likedUser.id
                    LEFT JOIN ProfileImageEntity whoLikeUserAvatar ON whoLikeUser.id = whoLikeUserAvatar.user.id AND whoLikeUserAvatar.sequence = 1
                    WHERE upl.likedUser.id = :likedUserId        
            """
    )
    List<UserProfileLikeDTO> findAllByLikedUser(UUID likedUserId);

}
