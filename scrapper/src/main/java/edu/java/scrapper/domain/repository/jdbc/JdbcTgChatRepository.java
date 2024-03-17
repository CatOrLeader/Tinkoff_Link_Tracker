package edu.java.scrapper.domain.repository.jdbc;

import edu.java.scrapper.domain.dto.TgChat;
import edu.java.scrapper.domain.repository.TgChatRepository;
import edu.java.scrapper.domain.repository.jdbc.mappers.TgChatRowMapper;
import java.util.List;
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
    public boolean register(long tgChatId) {
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
    public Optional<TgChat> find(long tgChatId) {
        return transactionTemplate.execute(status ->
            jdbcClient.sql("SELECT * FROM tg_chat WHERE id = ?")
                .param(String.valueOf(tgChatId))
                .query(tgChatRowMapper)
                .optional()
        );
    }

    @Override
    public boolean update(TgChat chat) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeTgChat = find(chat.getId());
            if (maybeTgChat.isEmpty()) {
                return false;
            }

            return jdbcClient.sql(
                    "UPDATE tg_chat SET dialog_state = :dialogState, language_tag = :languageTag WHERE id = :id")
                       .param("dialogState", chat.getDialogState())
                       .param("languageTag", chat.getLanguageTag())
                       .param("id", String.valueOf(chat.getId()))
                       .update() > 0;
        }));
    }

    @Override
    public List<TgChat> findAll() {
        return transactionTemplate.execute(status -> jdbcClient.sql("SELECT * FROM tg_chat")
            .query(tgChatRowMapper).list());
    }

    @Override
    public boolean remove(long tgChatId) {
        return Boolean.TRUE.equals(transactionTemplate.execute(status -> {
            var maybeTgChat = find(tgChatId);
            if (maybeTgChat.isEmpty()) {
                return false;
            }

            String strTgChatId = String.valueOf(tgChatId);

            boolean isDeleted = jdbcClient.sql("DELETE FROM tg_chat WHERE id = ?").param(strTgChatId).update() > 0;
            if (!isDeleted) {
                return false;
            }

            jdbcClient.sql("DELETE FROM chat_link_assignment WHERE chat_id = ?").param(strTgChatId).update();

            return true;
        }));
    }
}
