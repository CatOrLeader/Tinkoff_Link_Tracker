package edu.java.bot.rest.service;

import edu.java.bot.rest.model.AddLinkRequest;
import edu.java.bot.rest.model.LinkResponse;
import edu.java.bot.rest.model.ListLinksResponse;
import edu.java.bot.rest.model.RemoveLinkRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public final class LinksService {
    private static final String PATH = "/links";
    private static final String HEADER_NAME = "Tg-Chat-Id";
    private final WebClient scrapperWebClient;

    public Mono<ListLinksResponse> getLinks(long id) {
        return scrapperWebClient.get()
            .uri(PATH)
            .header(HEADER_NAME, String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinksResponse.class);
    }

    public Mono<LinkResponse> postLink(long id, @NotNull AddLinkRequest request) {
        return scrapperWebClient.post()
            .uri(PATH)
            .header(HEADER_NAME, String.valueOf(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }

    public Mono<LinkResponse> deleteLink(long id, @NotNull RemoveLinkRequest request) {
        return scrapperWebClient.method(HttpMethod.DELETE)
            .uri(PATH)
            .header(HEADER_NAME, String.valueOf(id))
            .bodyValue(request)
            .retrieve()
            .bodyToMono(LinkResponse.class);
    }
}
