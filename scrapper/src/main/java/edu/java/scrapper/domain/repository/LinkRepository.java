package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dto.Link;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;

public interface LinkRepository {
    @NotNull Link add(long tgChatId, @NotNull URI link);

    @NotNull Optional<Link> remove(long tgChatId, long linkId);

    @NotNull List<Link> findAll();

    @NotNull List<Link> findAllByTgChatId(long tgChatId);

    @NotNull Optional<Link> find(long linkId);

    @NotNull Optional<Link> find(@NotNull URI link);

    boolean update(@NotNull Link link);
}
