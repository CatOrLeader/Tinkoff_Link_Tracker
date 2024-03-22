package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.dto.TgChat;
import edu.java.scrapper.domain.repository.TgChatRepository;
import edu.java.scrapper.domain.repository.jdbc.mappers.TgChatRowMapper;
import jakarta.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;

@Repository
@RequiredArgsConstructor
public class JdbcTgChatRepository implements TgChatRepository {
    private final JdbcClient jdbcClient;
    private final TransactionTemplate transactionTemplate;
    private final TgChatRowMapper tgChatRowMapper;

    @Override
    public boolean register(String tgChatId) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeTgChat = find(tgChatId);
            if (maybeTgChat.isPresent()) {
                return false;
            }

            return jdbcClient.sql("INSERT INTO tg_chat VALUES (?, DEFAULT, NULL)")
                       .param(tgChatId)
                       .update() > 0;
        }));
    }

    @Override
    public Optional<TgChat> find(String tgChatId) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM tg_chat WHERE id = ?")
                .param(tgChatId)
                .query(tgChatRowMapper)
                .optional()
        );
    }

    @Override
    public Collection<TgChat> findAll() {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM tg_chat").query(tgChatRowMapper).list()
        );
    }

    @Override
    public Collection<TgChat> findAllByLinkUrl(String linkUrl) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM tg_chat, chat_link_assignment, link "
                           + "WHERE tg_chat.id = chat_link_assignment.chat_id AND "
                           + "chat_link_assignment.link_id = link.id AND link.uri = ?")
                .param(linkUrl)
                .query(tgChatRowMapper)
                .list()
        );
    }

    @Override
    public boolean update(@NotBlank String tgChatId, @NotBlank String dialogState, @NotBlank String languageTag) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeTgChat = find(tgChatId);
            if (maybeTgChat.isEmpty()) {
                return false;
            }

            return jdbcClient.sql(
                    "UPDATE tg_chat SET dialog_state = ?, language_tag = ? WHERE id = ?")
                       .params(dialogState, languageTag, tgChatId)
                       .update() > 0;
        }));
    }

    @Override
    public boolean remove(String tgChatId) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeTgChat = find(tgChatId);
            if (maybeTgChat.isEmpty()) {
                return false;
            }

            jdbcClient.sql("DELETE FROM chat_link_assignment WHERE chat_id = ?").param(tgChatId).update();

            return jdbcClient.sql("DELETE FROM tg_chat WHERE id = ?").param(tgChatId).update() > 0;
        }));
    }
}
