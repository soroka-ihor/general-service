package es.localchat.component.profile.avatar.repository;

import es.local.chat.sharedentities.model.profileimage.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProfileImageRepo extends JpaRepository<ProfileImageEntity, Long> {
    @Override
    Optional<ProfileImageEntity> findById(Long id);

    @Modifying
    @Query(value = "SELECT setval('profile_image_seq', (SELECT COALESCE(MAX(id), 1) FROM profile_image))", nativeQuery = true)
    void resetSequence();
}
