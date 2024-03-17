package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.service.LinkService;
import java.net.URI;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcLinkService implements LinkService {
    private final LinkRepository linkRepository;

    @Override
    public Link add(long tgChatId, URI url) {
        return linkRepository.add(tgChatId, url);
    }

    @Override
    public Link remove(long tgChatId, long linkId) {
        return linkRepository.remove(tgChatId, linkId).get();
    }

    @Override
    public Collection<Link> listAll(long tgChatId) {
        return linkRepository.findAllByTgChatId(tgChatId);
    }

    @Override
    public Collection<Link> findAll() {
        return linkRepository.findAll();
    }

    @Override
    public boolean update(Link link) {
        return linkRepository.update(link);
    }
}
