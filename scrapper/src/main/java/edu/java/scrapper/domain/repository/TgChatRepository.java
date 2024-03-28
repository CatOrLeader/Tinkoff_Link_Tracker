package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dto.TgChat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;

public interface TgChatRepository {
    boolean register(@NotBlank String tgChatId);

    @NotNull Optional<TgChat> find(@NotBlank String tgChatId);

    @NotNull Collection<TgChat> findAll();

    @NotNull Collection<TgChat> findAllByLinkUrl(@NotBlank String linkUrl);

    boolean update(@NotBlank String tgChatId, @NotBlank String dialogState, @NotBlank String languageTag);

    boolean remove(@NotBlank String tgChatId);
}
