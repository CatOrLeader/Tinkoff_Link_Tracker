package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.dto.TgChat;
import edu.java.scrapper.domain.repository.TgChatRepository;
import edu.java.scrapper.domain.service.TgChatService;
import java.net.URI;
import java.util.Collection;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JdbcTgChatService implements TgChatService {
    private final TgChatRepository tgChatRepository;

    @Override
    public boolean register(long tgChatId) {
        return tgChatRepository.register(String.valueOf(tgChatId));
    }

    @Override
    public boolean unregister(long tgChatId) {
        return tgChatRepository.remove(String.valueOf(tgChatId));
    }

    @Override
    public boolean update(TgChat chat) {
        return tgChatRepository.update(String.valueOf(chat.getId()), chat.getDialogState(), chat.getLanguageTag());
    }

    @Override
    public Optional<TgChat> find(long tgChatId) {
        return tgChatRepository.find(String.valueOf(tgChatId));
    }

    @Override
    public Collection<TgChat> findAllByLinkUrl(URI linkUrl) {
        return tgChatRepository.findAllByLinkUrl(linkUrl.toString());
    }
}
