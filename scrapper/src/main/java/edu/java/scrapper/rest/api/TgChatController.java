package edu.java.scrapper.rest.api;

import edu.java.scrapper.domain.service.TgChatService;
import edu.java.scrapper.rest.model.GetChatResponse;
import edu.java.scrapper.rest.model.UpdateChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TgChatController implements TgChatApi {
    private final TgChatService tgChatService;

    public ResponseEntity<Void> registerChat(long id) {
        log.info(String.format("New chat with id %s is registered", id));
        tgChatService.register(id);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetChatResponse> getChat(long id) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> updateChat(UpdateChatRequest request) {
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteChat(long id) {
        log.info(String.format("Chat with id %s is deleted", id));
        tgChatService.unregister(id);
        return ResponseEntity.ok().build();
    }
}
