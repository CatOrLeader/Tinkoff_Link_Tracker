package edu.java.scrapper.rest.api;

import edu.java.scrapper.domain.dto.TgChat;
import edu.java.scrapper.domain.service.TgChatService;
import edu.java.scrapper.rest.model.GetChatResponse;
import edu.java.scrapper.rest.model.UpdateChatRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@RequiredArgsConstructor
@Log4j2
public class TgChatController implements TgChatApi {
    private final TgChatService tgChatService;

    public ResponseEntity<Void> registerChat(long id) {
        if (!tgChatService.register(id)) {
            throw WebClientResponseException.Conflict.create(
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                null, null, null
            );
        }

        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<GetChatResponse> getChat(long id) {
        var chat = tgChatService.find(id).orElseThrow(() -> WebClientResponseException.NotFound.create(
            HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null, null, null
        ));

        return ResponseEntity.ok(new GetChatResponse(chat));
    }

    @Override
    public ResponseEntity<Void> updateChat(UpdateChatRequest request) {
        boolean isUpdated = tgChatService.update(new TgChat(request));
        if (!isUpdated) {
            throw WebClientResponseException.NotFound.create(
                HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null, null, null
            );
        }

        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> deleteChat(long id) {
        boolean isRemoved = tgChatService.unregister(id);
        if (!isRemoved) {
            throw WebClientResponseException.NotFound.create(
                HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(), null, null, null
            );
        }

        return ResponseEntity.ok().build();
    }
}
