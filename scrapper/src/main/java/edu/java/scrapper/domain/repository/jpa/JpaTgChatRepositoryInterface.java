package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.repository.jpa.dto.EntityTgChat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface JpaTgChatRepositoryInterface extends JpaRepository<EntityTgChat, String> {
    @Modifying
    @Transactional
    @Query(
        value = "INSERT INTO tg_chat VALUES (?1, DEFAULT, 'en')",
        nativeQuery = true
    )
    int register(@NotBlank String tgChatId);

    @Query(value = "from EntityTgChat chat inner join chat.links link where link.uri = ?1")
    @Transactional
    @NotNull
    Collection<EntityTgChat> findAllByUrl(@NotBlank URI linkUrl);

    @Modifying
    @Transactional
    @Query(
        value = "DELETE FROM tg_chat WHERE id = ?1",
        nativeQuery = true
    )
    int remove(@NotBlank String tgChatId);
}
