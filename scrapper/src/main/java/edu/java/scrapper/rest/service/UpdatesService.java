package edu.java.scrapper.rest.service;

import edu.java.scrapper.rest.model.LinkUpdate;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UpdatesService {
    private final WebClient botWebClient;

    public Void postLinkUpdate(@NotNull LinkUpdate update) {
        return botWebClient.post()
            .uri("/updates")
            .bodyValue(update)
            .retrieve()
            .onStatus(HttpStatusCode::isError, clientResponse -> {
                LogManager.getLogger().error(clientResponse);
                return Mono.error(RuntimeException::new);
            })
            .bodyToMono(Void.class)
            .block();
    }
}
