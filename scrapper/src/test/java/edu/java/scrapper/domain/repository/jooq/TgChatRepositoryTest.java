package edu.java.scrapper.domain.repository.jooq;

import edu.java.scrapper.IntegrationTest;
import edu.java.scrapper.domain.dto.TgChat;
import edu.java.scrapper.domain.repository.TgChatRepository;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TgChatRepositoryTest extends IntegrationTest {
    @Autowired
    private TgChatRepository tgChatRepository;

    @DynamicPropertySource
    static void properties(@NotNull DynamicPropertyRegistry registry) {
        registry.add("app.database-access-type", () -> "JOOQ");
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(1)
    void givenEmptyDB_whenFetchAnything_thenNothingFetched() {
        assertThat(tgChatRepository.find("1")).isEmpty();
        assertThat(tgChatRepository.findAll()).isEmpty();
        assertThat(tgChatRepository.findAllByLinkUrl("https://github.com")).isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(2)
    void givenEmptyDB_whenAddChat_thenChatIsAdded() {
        assertThat(tgChatRepository.register("1")).isTrue();

        var chat = tgChatRepository.find("1");
        assertThat(chat).isPresent().contains(chat.get());
        assertThat(tgChatRepository.findAll()).containsExactly(chat.get());
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(3)
    void givenDB_whenAddDuplicateChat_thenChatIsNotAdded() {
        assertThat(tgChatRepository.register("1")).isFalse();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(4)
    void whenUpdateExistingChat_thenUpdatedCorrectly() {
        TgChat chat = new TgChat(1L, "MAIN_MENU", "EN");
        boolean isUpdated =
            tgChatRepository.update(String.valueOf(chat.getId()), chat.getDialogState(), chat.getLanguageTag());

        assertThat(tgChatRepository.find("1")).contains(chat);
        assertThat(isUpdated).isTrue();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(5)
    void whenUpdateNonExistingChat_thenFalse() {
        TgChat chat = new TgChat(2L, "MAIN_MENU", "EN");

        assertThat(tgChatRepository.update(
            String.valueOf(chat.getId()),
            chat.getDialogState(),
            chat.getLanguageTag()
        )).isFalse();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(6)
    void whenRemoveExistingChat_thenTrue() {
        TgChat chat = new TgChat(1L, "MAIN_MENU", "EN");

        assertThat(tgChatRepository.remove(String.valueOf(chat.getId()))).isTrue();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(7)
    void whenRemoveNonExistingChat_thenFalse() {
        TgChat chat = new TgChat(1L, "MAIN_MENU", "EN");

        assertThat(tgChatRepository.remove(String.valueOf(chat.getId()))).isFalse();
    }
}
