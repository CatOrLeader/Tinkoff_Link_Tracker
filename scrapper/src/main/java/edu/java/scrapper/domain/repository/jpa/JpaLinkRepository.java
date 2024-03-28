package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.repository.LinkRepository;
import edu.java.scrapper.domain.repository.jpa.dto.EntityLink;
import edu.java.scrapper.utils.DateTimeUtils;
import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaLinkRepository implements LinkRepository {
    private final JpaLinkRepositoryInterface linkRepository;
    private final JpaTgChatRepositoryInterface tgChatRepository;

    @Override
    @Transactional
    public Collection<Link> findAll() {
        return linkRepository.findAll().stream()
            .map(Link::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Collection<Link> findAllByTgChatId(String tgChatId) {
        var tgChat = tgChatRepository.findById(tgChatId);

        return tgChat.<Collection<Link>>map(entityTgChat -> entityTgChat
            .getLinks().stream()
            .map(Link::new).collect(Collectors.toList())).orElse(new ArrayList<>());

    }

    @Override
    @Transactional
    public Collection<Link> findAllBefore(Timestamp dateTime) {
        return linkRepository
            .findAllByLastCheckedAtLessThanEqualOrLastCheckedAtIsNull(DateTimeUtils.parseFrom(dateTime))
            .stream()
            .map(Link::new).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<Link> findById(long linkId) {
        return linkRepository.findById(linkId)
            .map(Link::new);
    }

    @Override
    @Transactional
    public Optional<Link> find(String link) {
        return linkRepository.findByUri(URI.create(link)).map(Link::new);
    }

    @Override
    @Transactional
    public Optional<Link> add(String tgChatId, Link link) {
        var tgChat = tgChatRepository.findById(tgChatId).orElseThrow();
        var maybeLink = linkRepository.findByUri(link.getUri());

        if (maybeLink.isPresent()) {
            var fetchedLink = maybeLink.get();

            if (fetchedLink.getTgChats().contains(tgChat)) {
                return Optional.empty();
            } else {
                fetchedLink.getTgChats().add(tgChat);
                tgChat.getLinks().add(fetchedLink);
                return Optional.of(fetchedLink).map(Link::new);
            }
        }

        return Optional.of(linkRepository.saveAndFlush(new EntityLink(link, tgChat))).map(Link::new);
    }

    @Override
    @Transactional
    public Optional<Link> remove(String tgChatId, long linkId) {
        return linkRepository.findById(linkId).filter(
            link -> {
                link.getTgChats().removeIf(tgChat -> {
                    if (tgChat.getId().equals(tgChatId)) {
                        tgChat.getLinks().removeIf(link1 -> link1.getId() == linkId);
                        return true;
                    }
                    return false;
                });
                linkRepository.saveAndFlush(link);
                if (link.getTgChats().isEmpty()) {
                    linkRepository.delete(link);
                }
                return true;
            }
        ).map(Link::new);
    }

    @Override
    @Transactional
    public boolean update(Link link) {
        return linkRepository.findByUri(link.getUri())
            .filter(link1 -> {
                link1.updateFrom(link);
                return true;
            }).isPresent();
    }

    @Override
    @Transactional
    public boolean updateEtag(String linkUri, String etag) {
        return linkRepository.findByUri(URI.create(linkUri))
            .filter(link -> {
                link.updateEtag(etag);
                return true;
            }).isPresent();
    }
}
