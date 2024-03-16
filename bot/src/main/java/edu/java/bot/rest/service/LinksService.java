package edu.java.bot.rest.service;

import edu.java.bot.dialog.data.Link;
import edu.java.bot.rest.model.AddLinkRequest;
import edu.java.bot.rest.model.LinkResponse;
import edu.java.bot.rest.model.ListLinksResponse;
import edu.java.bot.rest.model.RemoveLinkRequest;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public final class LinksService {
    private static final String PATH = "/links";
    private static final String HEADER_NAME = "Tg-Chat-Id";
    private final WebClient scrapperWebClient;

    public Optional<List<Link>> getLinks(long id) {
        var response = scrapperWebClient.get()
            .uri(PATH)
            .header(HEADER_NAME, String.valueOf(id))
            .retrieve()
            .bodyToMono(ListLinksResponse.class)
            .block();

        return response.size() == 0 ? Optional.empty() : Optional.of(
            response.links().stream()
                .map(Link::new)
                .collect(Collectors.toList())
        );
    }

    public Link postLink(long id, @NotNull Link link) {
        return scrapperWebClient.post()
            .uri(PATH)
            .header(HEADER_NAME, String.valueOf(id))
            .bodyValue(new AddLinkRequest(link))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .map(Link::new)
            .block();
    }

    public Link deleteLink(long tgChatId, long linkId) {
        return scrapperWebClient.method(HttpMethod.DELETE)
            .uri(PATH)
            .header(HEADER_NAME, String.valueOf(tgChatId))
            .bodyValue(new RemoveLinkRequest(linkId))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .map(Link::new)
            .block();
    }
}
