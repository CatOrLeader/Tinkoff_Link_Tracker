package edu.java.scrapper.rest.api;

import edu.java.scrapper.domain.service.LinkService;
import edu.java.scrapper.rest.model.AddLinkRequest;
import edu.java.scrapper.rest.model.LinkResponse;
import edu.java.scrapper.rest.model.ListLinksResponse;
import edu.java.scrapper.rest.model.RemoveLinkRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LinksController implements LinksApi {
    private final LinkService linkService;

    public ResponseEntity<ListLinksResponse> getLinks(long tgChatId) {
        log.info(String.format("Return all links of %d chat", tgChatId));
        List<LinkResponse> list = new ArrayList<>(linkService.listAll(tgChatId)).stream()
            .map(link -> new LinkResponse(link.getId(), link.getUri()))
            .collect(Collectors.toList());
        return ResponseEntity.ok(new ListLinksResponse(
            list,
            list.size()
        ));
    }

    public ResponseEntity<LinkResponse> addLink(
        long tgChatId,
        AddLinkRequest request
    ) {
        log.info(String.format("Link %s was successfully added to the chat %d", request.link(), tgChatId));
        var link = linkService.add(tgChatId, request.link());
        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUri()));
    }

    public ResponseEntity<LinkResponse> deleteLink(
        long tgChatId,
        RemoveLinkRequest request
    ) {
        log.info(String.format("Link %s was successfully deleted from the chat %d", request.linkId(), tgChatId));
        var link = linkService.remove(tgChatId, request.linkId());
        return ResponseEntity.ok(new LinkResponse(link.getId(), link.getUri()));
    }
}
