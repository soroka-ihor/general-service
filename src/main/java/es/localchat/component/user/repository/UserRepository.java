package es.localchat.component.user.repository;

import es.local.chat.sharedentities.model.user.UserEntity;
import es.localchat.component.user.model.OnlineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query(
            """
                SELECT u FROM UserEntity u
                JOIN FETCH u.profileImages
                WHERE u.UUID = :userUUID
            """
    )
    Optional<UserEntity> getByUUID(String userUUID);

    Optional<UserEntity> findByUUID(String uuid);
    Optional<UserEntity> findById(UUID id);
    Optional<UserEntity> findByUsername(String username);
    boolean existsByUUID(String uuid);
    boolean existsByUsername(String username);

    @Query(
            value = """
                SELECT ST_Distance(u1.point, u2.point)
                FROM client u1, client u2 
                WHERE u1.id = :userId1 AND u2.id = :userId2
            """, nativeQuery = true
    ) Double findDistanceBetween(UUID userId1, UUID userId2);


    @Query("""
            SELECT user FROM UserEntity user 
            LEFT JOIN FETCH user.profileImages
            LEFT JOIN FETCH user.customAbout
            WHERE user.id = :id
            """
    ) Optional<UserEntity> getFullUserById(UUID id);


    @Query(
            """
                SELECT new es.localchat.component.user.model.OnlineStatus(user.isOnline, user.recentActivity)
                FROM UserEntity user 
                WHERE user.id = :userId
            """
    ) Optional<OnlineStatus> getOnlineStatus(UUID userId);
}
