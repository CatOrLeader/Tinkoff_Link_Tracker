package edu.java.scrapper.domain.service;

import edu.java.scrapper.domain.dto.TgChat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;

public interface TgChatService {
    boolean register(long tgChatId);

    boolean unregister(long tgChatId);

    boolean update(@NotNull TgChat chat);

    @NotNull Optional<TgChat> find(long tgChatId);

    @NotNull Collection<TgChat> findAllByLinkUrl(@NotBlank URI linkUrl);
}
