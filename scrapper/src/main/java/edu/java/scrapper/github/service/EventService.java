package edu.java.scrapper.github.service;

import edu.java.scrapper.github.model.IssueResponse;
import edu.java.scrapper.github.model.PullResponse;
import edu.java.scrapper.github.model.RepoResponse;
import jakarta.validation.constraints.NotBlank;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventService {
    private final WebClient githubWebClient;

    public PullResponse getPullByOwnerNameNumber(@NotBlank String owner, @NotBlank String name, int number) {
        return githubWebClient
            .get()
            .uri("/repos/{owner}/{name}/pulls/{number}", owner, name, number)
            .retrieve().bodyToMono(PullResponse.class)
            .doOnError(throwable -> log.error(throwable.toString()))
            .block();
    }

    public IssueResponse getIssueByOwnerNameNumber(
        @NotBlank String owner,
        @NotBlank String name,
        int number
    ) {
        return githubWebClient
            .get()
            .uri("/repos/{owner}/{name}/issues/{number}", owner, name, number)
            .retrieve().bodyToMono(IssueResponse.class)
            .doOnError(throwable -> log.error(throwable.toString()))
            .block();
    }

    public RepoResponse getRepoLastEventByOwnerName(@NotBlank String owner, @NotBlank String name) {
        return githubWebClient
            .get()
            .uri("/repos/{owner}/{name}/events?per_page=1", owner, name)
            .retrieve()
            .bodyToMono(RepoResponse[].class)
            .doOnError(throwable -> log.error(throwable.toString()))
            .blockOptional()
            .orElseThrow(
                () -> new NoSuchElementException(String.format(
                    "There is no event for the repository %s of the owner %s",
                    name,
                    owner
                )))[0];
    }
}
