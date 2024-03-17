package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.IntegrationTest;
import java.sql.SQLException;
import edu.java.scrapper.domain.dto.TgChat;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JdbcTgChatRepositoryTest extends IntegrationTest {
    @Autowired
    private JdbcTgChatRepository tgChatRepository;

    @Test
    @Transactional
    @Rollback(false)
    @Order(1)
    void givenEmptyDB_whenFetchAnything_thenNothingFetched() {
        assertThat(tgChatRepository.find(1L)).isEmpty();
        assertThat(tgChatRepository.findAll()).isEmpty();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(2)
    void givenEmptyDB_whenAddChat_thenChatIsAdded() {
        assertThat(tgChatRepository.register(1L)).isTrue();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(3)
    void givenDB_whenAddDuplicateChat_thenChatIsNotAdded() {
        assertThat(tgChatRepository.register(1L)).isFalse();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(4)
    void whenUpdateExistingChat_thenUpdatedCorrectly() {
        TgChat chat = new TgChat(1L, "MAIN_MENU", "EN");
        boolean isUpdated = tgChatRepository.update(chat);

        assertThat(tgChatRepository.find(1L)).contains(chat);
        assertThat(isUpdated).isTrue();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(5)
    void whenUpdateNonExistingChat_thenFalse() {
        TgChat chat = new TgChat(2L, "MAIN_MENU", "EN");

        assertThat(tgChatRepository.update(chat)).isFalse();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(6)
    void whenRemoveExistingChat_thenFalse() {
        TgChat chat = new TgChat(1L, "MAIN_MENU", "EN");

        assertThat(tgChatRepository.remove(chat.getId())).isTrue();
    }

    @Test
    @Transactional
    @Rollback(false)
    @Order(7)
    void whenRemoveNonExistingChat_thenFalse() {
        TgChat chat = new TgChat(1L, "MAIN_MENU", "EN");

        assertThat(tgChatRepository.remove(chat.getId())).isFalse();
    }
}
