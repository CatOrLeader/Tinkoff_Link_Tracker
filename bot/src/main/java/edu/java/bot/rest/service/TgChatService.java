package edu.java.bot.rest.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public final class TgChatService {
    private static final String PATH = "/tg-chat/{id}";

    private final WebClient scrapperWebClient;

    public Mono<Void> registerNewChat(long id) {
        return scrapperWebClient.post()
            .uri(PATH, id)
            .retrieve()
            .bodyToMono(Void.class);
    }

    public Mono<Void> deleteChat(long id) {
        return scrapperWebClient.delete()
            .uri(PATH, id)
            .retrieve()
            .bodyToMono(Void.class);
    }
}
