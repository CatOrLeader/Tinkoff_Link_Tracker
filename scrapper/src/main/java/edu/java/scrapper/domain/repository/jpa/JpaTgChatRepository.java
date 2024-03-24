package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.dto.TgChat;
import edu.java.scrapper.domain.repository.TgChatRepository;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class JpaTgChatRepository implements TgChatRepository {
    private final JpaTgChatRepositoryInterface tgChatRepository;

    @Override
    @Transactional
    public boolean register(String tgChatId) {
        if (tgChatRepository.findById(tgChatId).isEmpty()) {
            return tgChatRepository.register(tgChatId) > 0;
        }

        return false;
    }

    @Override
    @Transactional
    public Optional<TgChat> find(String tgChatId) {
        return tgChatRepository.findById(tgChatId).map(TgChat::new);
    }

    @Override
    @Transactional
    public Collection<TgChat> findAll() {
        return tgChatRepository.findAll().stream()
            .map(TgChat::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Collection<TgChat> findAllByLinkUrl(String linkUrl) {
        return tgChatRepository.findAllByUrl(URI.create(linkUrl)).stream()
            .map(TgChat::new)
            .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean update(String tgChatId, String dialogState, String languageTag) {
        return tgChatRepository.findById(tgChatId).filter(tgChat -> {
            tgChat.setDialogState(dialogState);
            tgChat.setLanguageTag(languageTag);
            return true;
        }).isPresent();
    }

    @Override
    @Transactional
    public boolean remove(String tgChatId) {
        var tgChat = tgChatRepository.findById(tgChatId);

        if (tgChat.isEmpty()) {
            return false;
        }

        tgChat.get().getLinks().clear();
        tgChatRepository.flush();

        return tgChatRepository.remove(tgChatId) > 0;
    }
}
