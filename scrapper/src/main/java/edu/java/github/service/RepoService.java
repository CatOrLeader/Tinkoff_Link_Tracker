package edu.java.github.service;

import edu.java.github.response.RepoResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@AllArgsConstructor
public class RepoService {
    private final WebClient githubWebClient;

    public RepoResponse getRepoByOwnerNameNumber(@NotEmpty String owner, @NotEmpty String name, int number) {
        return githubWebClient.get()
            .uri("/repos/{owner}/{name}/pulls/{number}", owner, name, number)
            .accept(MediaType.APPLICATION_JSON).retrieve().bodyToMono(RepoResponse.class)
            .doOnError(throwable -> LogManager.getLogger().error(throwable))
            .block();
    }
}
