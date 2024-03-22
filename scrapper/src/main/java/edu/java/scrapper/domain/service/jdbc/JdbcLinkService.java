package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.utils.DateTimeUtils;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;

    @Override
    public Optional<Link> find(URI linkUri) {
        return linkRepository.find(linkUri.toString());
    }

    @Override
    public Collection<Link> findAllByTgId(long tgChatId) {
        return linkRepository.findAllByTgChatId(String.valueOf(tgChatId));
    }

    @Override
    public Collection<Link> findAllBefore(OffsetDateTime dateTime) {
        return linkRepository.findAllBefore(DateTimeUtils.parseFrom(dateTime));
    }

    @Override
    public Optional<Link> add(long tgChatId, Link link) {
        return linkRepository.add(String.valueOf(tgChatId), link);
    }

    @Override
    public Optional<Link> remove(long tgChatId, long linkId) {
        return linkRepository.remove(String.valueOf(tgChatId), linkId);
    }

    @Override
    public boolean update(Link link) {
        return linkRepository.update(link);
    }

    @Override
    public boolean updateEtag(URI linkUri, String etag) {
        return linkRepository.updateEtag(linkUri.toString(), etag);
    }
}
