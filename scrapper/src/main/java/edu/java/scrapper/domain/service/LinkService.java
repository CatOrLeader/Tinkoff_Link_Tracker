package edu.java.scrapper.domain.service;

import edu.java.scrapper.domain.dto.Link;
import java.net.URI;
import java.util.Collection;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;

@Service
public interface LinkService {
    Link add(long tgChatId, URI url);

    Link remove(long tgChatId, long linkId);

    Collection<Link> listAll(long tgChatId);

    Collection<Link> findAll();

    boolean update(@NotNull Link link);
}
