package edu.java.scrapper.rest.api;

import edu.java.scrapper.rest.model.AddLinkRequest;
import edu.java.scrapper.rest.model.LinkResponse;
import edu.java.scrapper.rest.model.ListLinksResponse;
import edu.java.scrapper.rest.model.RemoveLinkRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LinksController implements LinksApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinksController.class);

    public ResponseEntity<ListLinksResponse> getLinks(long tgChatId) {
        LOGGER.info(String.format("Return all links of %d chat", tgChatId));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<LinkResponse> addLink(
        long tgChatId,
        AddLinkRequest request
    ) {
        LOGGER.info(String.format("Link %s was successfully added to the chat %d", request.link(), tgChatId));
        return ResponseEntity.ok(new LinkResponse(0, request.link()));
    }

    public ResponseEntity<LinkResponse> deleteLink(
        long tgChatId,
        RemoveLinkRequest request
    ) {
        LOGGER.info(String.format("Link %s was successfully deleted from the chat %d", request.link(), tgChatId));
        return ResponseEntity.ok(new LinkResponse(0, request.link()));
    }
}
