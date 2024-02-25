package edu.java.scrapper.api.rest;

import edu.java.scrapper.api.model.AddLinkRequest;
import edu.java.scrapper.api.model.LinkResponse;
import edu.java.scrapper.api.model.ListLinksResponse;
import edu.java.scrapper.api.model.RemoveLinkRequest;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/links")
public class LinksController implements LinksApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinksController.class);

    public ResponseEntity<ListLinksResponse> getLinks(@RequestHeader("Tg-Chat-Id") int tgChatId) {
        LOGGER.info(String.format("Return all links of %d chat", tgChatId));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<LinkResponse> addLink(
        @RequestHeader("Tg-Chat-Id") int tgChatId,
        @RequestBody AddLinkRequest request
    ) {
        LOGGER.info(String.format("Link %s was successfully added to the chat %d", request.link(), tgChatId));
        return ResponseEntity.ok(new LinkResponse(0, URI.create(request.link())));
    }

    public ResponseEntity<LinkResponse> deleteLink(
        @RequestHeader("Tg-Chat-Id") int tgChatId,
        @RequestBody RemoveLinkRequest request
    ) {
        LOGGER.info(String.format("Link %s was successfully deleted from the chat %d", request.link(), tgChatId));
        return ResponseEntity.ok(new LinkResponse(0, URI.create(request.link())));
    }
}
