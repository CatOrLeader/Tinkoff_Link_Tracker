package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dto.TgChat;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

public interface TgChatRepository {
    boolean register(long tgChatId);

    @NotNull Optional<TgChat> find(long tgChatId);

    boolean update(@NotNull TgChat chat);

    @NotNull List<TgChat> findAll();

    boolean remove(long tgChatId);
}
