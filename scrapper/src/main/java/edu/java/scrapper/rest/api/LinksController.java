package edu.java.scrapper.rest.api;

import edu.java.scrapper.rest.model.AddLinkRequest;
import edu.java.scrapper.rest.model.LinkResponse;
import edu.java.scrapper.rest.model.ListLinksResponse;
import edu.java.scrapper.rest.model.RemoveLinkRequest;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class LinksController implements LinksApi {
    public ResponseEntity<ListLinksResponse> getLinks(long tgChatId) {
        log.info(String.format("Return all links of %d chat", tgChatId));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<LinkResponse> addLink(
        long tgChatId,
        AddLinkRequest request
    ) {
        log.info(String.format("Link %s was successfully added to the chat %d", request.link(), tgChatId));
        return ResponseEntity.ok(new LinkResponse(0, request.link()));
    }

    public ResponseEntity<LinkResponse> deleteLink(
        long tgChatId,
        RemoveLinkRequest request
    ) {
        log.info(String.format("Link %s was successfully deleted from the chat %d", request.linkId(), tgChatId));
        return ResponseEntity.ok(new LinkResponse(0, URI.create("https://github.com")));
    }
}
