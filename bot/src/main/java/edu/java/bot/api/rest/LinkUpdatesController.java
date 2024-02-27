package edu.java.bot.api.rest;

import edu.java.bot.api.model.LinkUpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/updates")
public class LinkUpdatesController implements LinkUpdatesApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(LinkUpdatesController.class);

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdateRequest request) {
        LOGGER.info("Updates are proceeded: " + request);
        return ResponseEntity.ok().build();
    }
}
