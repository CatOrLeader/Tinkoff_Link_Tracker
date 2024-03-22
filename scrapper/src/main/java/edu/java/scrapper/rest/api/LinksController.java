package edu.java.scrapper.rest.api;

import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.domain.service.TgChatService;
import edu.java.scrapper.github.service.EventService;
import edu.java.scrapper.rest.api.exception.LinkUnsupportedException;
import edu.java.scrapper.rest.model.AddLinkRequest;
import edu.java.scrapper.rest.model.LinkResponse;
import edu.java.scrapper.rest.model.ListLinksResponse;
import edu.java.scrapper.rest.model.RemoveLinkRequest;
import edu.java.scrapper.stackoverflow.service.QuestionService;
import edu.java.scrapper.utils.LinkUtils;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequiredArgsConstructor
@Log4j2
public class LinksController implements LinksApi {
    private final LinkService linkService;
    private final TgChatService tgChatService;
    private final EventService eventService;
    private final QuestionService questionService;

    public ResponseEntity<ListLinksResponse> getLinks(long tgChatId) {
        checkIfTgChatExists(tgChatId);

        return ResponseEntity.ok(new ListLinksResponse(
            linkService.findAllByTgId(tgChatId).stream()
                .map(LinkResponse::new)
                .collect(Collectors.toList())
        ));
    }

    public ResponseEntity<LinkResponse> addLink(
        long tgChatId,
        AddLinkRequest request
    ) {
        checkIfTgChatExists(tgChatId);

        URI receivedLinkUri = request.link();
        var fetchedFromSourceLink = fetchLink(receivedLinkUri);

        var inDBLink = linkService.add(tgChatId, fetchedFromSourceLink).orElseThrow(() ->
            WebClientResponseException.Conflict.create(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                null, null, null
            )
        );

        return ResponseEntity.ok(new LinkResponse(inDBLink.getId(), inDBLink.getUri()));
    }

    public ResponseEntity<LinkResponse> deleteLink(
        long tgChatId,
        RemoveLinkRequest request
    ) {
        checkIfTgChatExists(tgChatId);

        var link =
            linkService.remove(tgChatId, request.linkId()).orElseThrow(() -> WebClientResponseException.NotFound.create(
                HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase() + ": link", null, null, null
            ));

        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUri()));
    }

    private void checkIfTgChatExists(long tgChatId) throws HttpClientErrorException {
        tgChatService.find(tgChatId).orElseThrow(() -> WebClientResponseException.NotFound.create(
            HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase() + ": tg-chat", null, null, null
        ));
    }

    private Link fetchLink(URI link) {
        var responseType = LinkUtils.getResponseType(link).orElseThrow(LinkUnsupportedException::new);

        switch (responseType) {
            case GITHUB_ISSUE -> {
                List<String> params = LinkUtils.extractOwnerNameNumber(link.toString());

                return
                    eventService.getIssueByOwnerNameNumber(
                            link,
                            params.getFirst(),
                            params.get(1),
                            Integer.parseInt(params.getLast())
                        )
                        .map(Link::new)
                        .orElseGet(() -> linkService.find(link).orElseThrow(() -> new InternalError(
                            "Something went wrong when firstly fetching the issue"))
                        );
            }

            case GITHUB_PULL -> {
                List<String> params = LinkUtils.extractOwnerNameNumber(link.toString());

                return
                    eventService.getPullByOwnerNameNumber(
                            link,
                            params.getFirst(),
                            params.get(1),
                            Integer.parseInt(params.getLast())
                        )
                        .map(Link::new)
                        .orElseGet(() -> linkService.find(link)
                            .orElseThrow(() ->
                                new InternalError("Something went wrong when firstly fetching the repo")));

            }

            case SFO_QUESTION -> {
                return
                    new Link(
                        questionService.getQuestionById(LinkUtils.extractQuestionId(link.toString()))
                    );
            }

            case null, default -> throw new InternalError("Something went wrong while parsing link type");
        }
    }
}
