package edu.java.scrapper.github.service;

import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.github.model.IssueResponse;
import edu.java.scrapper.github.model.PullResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class EventService {
    private final WebClient githubWebClient;
    private final LinkService linkService;

    public Optional<PullResponse> getPullByOwnerNameNumber(
        @NotNull URI originalUrl,
        @NotBlank String owner,
        @NotBlank String name,
        int number
    ) {
        var link = linkService.find(originalUrl).orElse(null);

        try {
            return Optional.ofNullable(githubWebClient
                .get()
                .uri("/repos/{owner}/{name}/pulls/{number}", owner, name, number)
                .header(HttpHeaders.IF_NONE_MATCH, link == null ? "" : link.getEtag())
                .retrieve()
                .toEntity(PullResponse.class)
                .doOnError(throwable -> log.error(throwable.toString()))
                .filter(entity -> entity.getStatusCode().is2xxSuccessful()
                                  || entity.getStatusCode().is3xxRedirection())
                .flatMap(entity -> {
                    if (entity.getStatusCode().is3xxRedirection()) {
                        return Mono.empty();
                    }

                    linkService.updateEtag(originalUrl, entity.getHeaders().getETag());
                    return Mono.justOrEmpty(entity.getBody());
                })
                .block());
        } catch (NullPointerException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    public Optional<IssueResponse> getIssueByOwnerNameNumber(
        @NotNull URI originalUrl,
        @NotBlank String owner,
        @NotBlank String name,
        int number
    ) {
        var link = linkService.find(originalUrl).orElse(null);

        try {
            return Optional.ofNullable(githubWebClient
                .get()
                .uri("/repos/{owner}/{name}/issues/{number}", owner, name, number)
                .header(HttpHeaders.IF_NONE_MATCH, link == null ? "" : link.getEtag())
                .retrieve()
                .toEntity(IssueResponse.class)
                .doOnError(throwable -> log.error(throwable.toString()))
                .filter(entity -> entity.getStatusCode().is2xxSuccessful()
                                  || entity.getStatusCode().is3xxRedirection())
                .flatMap(entity -> {
                    if (entity.getStatusCode().is3xxRedirection()) {
                        return Mono.empty();
                    }

                    linkService.updateEtag(originalUrl, entity.getHeaders().getETag());
                    return Mono.justOrEmpty(entity.getBody());
                })
                .block());
        } catch (NullPointerException e) {
            log.error(e.getMessage(), e);
            return Optional.empty();
        }
    }
}
