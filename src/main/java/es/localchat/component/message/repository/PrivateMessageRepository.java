package es.localchat.component.message.repository;

import es.local.chat.sharedentities.model.message.direct.PrivateMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {

    @Query(
            value = "select * from private_message where private_chat_id = :chatId and created_at < :before",
            nativeQuery = true
    )
    List<PrivateMessage> findByChatId(Long chatId, ZonedDateTime before, Pageable page);

    @Query(
            value = "select count(id) from private_message where private_chat_id = :chatId and created_at < :before",
            nativeQuery = true
    )
    int countMessagesByChatId(Long chatId, ZonedDateTime before);

    @Query(
            value = """
                    select * from private_message where private_chat_id = :chatId
                    and created_at = (select max(created_at) from private_message where private_chat_id = :chatId)
                    """,
            nativeQuery = true
    )
    Optional<PrivateMessage> findLastMessageInChat(Long chatId);

    Optional<PrivateMessage> findById(UUID id);

    @Query(
            value = """
                        select * from private_message where id = :messageId and client_id = :userId
                    """, nativeQuery = true
    )
    Optional<PrivateMessage> findByIdAndUserId(UUID messageId, UUID userId);
}
