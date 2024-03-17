package edu.java.scrapper.domain.service.jdbc;

import edu.java.scrapper.domain.repository.TgChatRepository;
import edu.java.scrapper.domain.service.TgChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TgChatLinkService implements TgChatService {
    private final TgChatRepository tgChatRepository;

    @Override
    public void register(long tgChatId) {
        tgChatRepository.register(tgChatId);
    }

    @Override
    public void unregister(long tgChatId) {
        tgChatRepository.register(tgChatId);
    }
}
