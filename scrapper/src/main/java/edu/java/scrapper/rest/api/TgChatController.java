package edu.java.scrapper.rest.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TgChatController implements TgChatApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TgChatController.class);

    public ResponseEntity<Void> registerChat(long id) {
        LOGGER.info(String.format("New chat with id %s is registered", id));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteChat(long id) {
        LOGGER.info(String.format("Chat with id %s is deleted", id));
        return ResponseEntity.ok().build();
    }
}
