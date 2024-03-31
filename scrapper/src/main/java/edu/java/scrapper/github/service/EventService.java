package edu.java.scrapper.github.service;

import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.github.model.IssueEventResponse;
import edu.java.scrapper.github.model.IssueResponse;
import edu.java.scrapper.github.model.PullResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
@RequiredArgsConstructor
public class EventService {
    private final WebClient githubWebClient;
    private final LinkService linkService;

    @Retryable(interceptor = "primaryRetryTemplate")
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
                .doOnError(log::error)
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

    @Retryable
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
                .doOnError(log::error)
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

    public Optional<IssueEventResponse> getIssueLastEventByOwnerNameNumber(
        @NotBlank String owner,
        @NotBlank String name,
        int number
    ) {
        ParameterizedTypeReference<List<IssueEventResponse>> type = new ParameterizedTypeReference<>() {
        };

        try {
            return Optional.ofNullable(githubWebClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/repos/{owner}/{name}/issues/{number}/events")
                    .queryParam("per_page", 1)
                    .build(owner, name, number))
                .retrieve()
                .toEntity(type)
                .doOnError(log::error)
                .flatMap(nested -> {
                    var body = nested.getBody();
                    if (body == null) {
                        log.warn("Body is missing");
                        return Mono.empty();
                    }

                    return Mono.justOrEmpty(body.getFirst());
                })
                .block());
        } catch (NullPointerException e) {
            log.error(e);
            return Optional.empty();
        }
    }
}
