package edu.java.scrapper.api.rest;

import jakarta.validation.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tg-chat")
public class TgChatController implements TgChatApi {
    private static final Logger LOGGER = LoggerFactory.getLogger(TgChatController.class);

    public ResponseEntity<Void> registerChat(@PathVariable int id) {
        LOGGER.info(String.format("New chat with id %s is registered", id));
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteChat(@PathVariable int id) {
        LOGGER.info(String.format("Chat with id %s is deleted", id));
        return ResponseEntity.ok().build();
    }
}
