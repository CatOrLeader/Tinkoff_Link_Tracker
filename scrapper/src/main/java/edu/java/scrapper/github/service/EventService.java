package edu.java.scrapper.github.service;

import edu.java.scrapper.github.model.PullResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class EventService {
    private final WebClient githubWebClient;

    public PullResponse getPullByOwnerNameNumber(@NotEmpty String owner, @NotEmpty String name, int number) {
        return githubWebClient
            .get()
            .uri("/repos/{owner}/{name}/pulls/{number}", owner, name, number)
            .retrieve().bodyToMono(PullResponse.class)
            .doOnError(throwable -> LogManager.getLogger().error(throwable))
            .block();
    }
}
