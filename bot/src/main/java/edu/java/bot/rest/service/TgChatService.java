package edu.java.bot.rest.service;

import edu.java.bot.dialog.data.UserData;
import edu.java.bot.rest.model.GetChatResponse;
import edu.java.bot.rest.model.UpdateChatRequest;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public final class TgChatService {
    private static final String ROOT_PATH = "/tg-chat";
    private static final String PATH = ROOT_PATH + "/{id}";

    private final WebClient scrapperWebClient;

    public Void registerNewChat(long id) {
        return scrapperWebClient.post()
            .uri(PATH, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public @NotNull Optional<UserData> getChat(long id) {
        return scrapperWebClient.get()
            .uri(PATH, id)
            .retrieve()
            .bodyToMono(GetChatResponse.class)
            .map(UserData::new)
            .blockOptional();
    }

    public Void updateChat(@NotNull UserData request) {
        UpdateChatRequest requestToSend = new UpdateChatRequest(request);

        return scrapperWebClient.post()
            .uri(ROOT_PATH)
            .bodyValue(requestToSend)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

    public Void deleteChat(long id) {
        return scrapperWebClient.delete()
            .uri(PATH, id)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }
}
