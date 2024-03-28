package edu.java.scrapper.domain.service;

import edu.java.scrapper.domain.dto.Link;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface LinkService {
    @NotNull Optional<Link> find(URI linkUri);

    @NotNull Collection<Link> findAllByTgId(long tgChatId);

    @NotNull Collection<Link> findAllBefore(@NotNull OffsetDateTime dateTime);

    @NotNull Optional<Link> add(long tgChatId, @NotNull Link link);

    @NotNull Optional<Link> remove(long tgChatId, long linkId);

    boolean update(@NotNull Link link);

    boolean updateEtag(@NotNull URI linkUri, @NotBlank String etag);
}
