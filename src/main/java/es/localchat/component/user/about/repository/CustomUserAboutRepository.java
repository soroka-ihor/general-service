package es.localchat.component.user.about.repository;

import es.local.chat.sharedentities.model.customabout.CustomUserAbout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomUserAboutRepository extends JpaRepository<CustomUserAbout, Long> {

    @Query(
            value =
                    """
                        select count(*) from custom_user_about cua
                        join custom_user_about2user cu2u ON cua.id = cu2u.custom_user_about_id
                        where cu2u.user_id = :userId
                    """,
            nativeQuery = true
    )
    int countByUser(UUID userId);

    Optional<CustomUserAbout> findByText(String text);

    @Query(
            value =
                    """
                        select cua.id, cua.text from custom_user_about cua
                        join custom_user_about2user cu2u ON cua.id = cu2u.custom_user_about_id
                        where cu2u.user_id = :userId
                    """,
            nativeQuery = true
    )
    List<CustomUserAbout> findAllByUserId(UUID userId);

    @Query(
            value =
                    """
                        select cua.id, cua.text from custom_user_about cua
                        join custom_user_about2user cu2u ON cua.id = cu2u.custom_user_about_id
                        where cu2u.user_id = :userId and cua.text = :text
                    """,
            nativeQuery = true
    )
    Optional<CustomUserAbout> findByUserIdAndText(UUID userId, String text);

    @Query(
            value =
                    """
                        select count(*) from custom_user_about cua
                        join custom_user_about2user cu2u ON cua.id = cu2u.custom_user_about_id
                        where cua.text = :customAboutText
                    """,
            nativeQuery = true
    ) int countOfUsersUsedCustomAbout(String customAboutText);


}
