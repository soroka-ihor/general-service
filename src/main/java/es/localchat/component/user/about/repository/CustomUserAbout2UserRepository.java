package es.localchat.component.user.about.repository;

import es.local.chat.sharedentities.model.customabout.CustomUserAbout2User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CustomUserAbout2UserRepository extends JpaRepository<CustomUserAbout2User, Long> {
    Optional<CustomUserAbout2User> findByUserIdAndCustomUserAboutId(UUID userId, Long customUserAboutId);
    void deleteByUserIdAndCustomUserAboutId(UUID userId, Long customUserAboutId);
}
