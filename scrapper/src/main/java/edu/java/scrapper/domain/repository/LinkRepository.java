package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dto.Link;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Optional;

public interface LinkRepository {
    @NotNull Collection<Link> findAll();

    @NotNull Collection<Link> findAllByTgChatId(@NotBlank String tgChatId);

    @NotNull Collection<Link> findAllBefore(@NotBlank Timestamp dateTime);

    @NotNull Optional<Link> findById(long linkId);

    @NotNull Optional<Link> find(String link);

    @NotNull Optional<Link> add(@NotBlank String tgChatId, @NotNull Link link);

    @NotNull Optional<Link> remove(@NotBlank String tgChatId, @NotBlank long linkId);

    boolean update(@NotNull Link link);

    boolean updateEtag(@NotBlank String linkUri, @NotBlank String etag);
}
