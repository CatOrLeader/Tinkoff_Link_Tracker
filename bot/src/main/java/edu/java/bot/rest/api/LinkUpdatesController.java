package edu.java.bot.rest.api;

import edu.java.bot.dialog.service.LinkUpdateNotifier;
import edu.java.bot.rest.model.LinkUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LinkUpdatesController implements LinkUpdatesApi {
    private final LinkUpdateNotifier notifier;

    @Override
    public ResponseEntity<Void> updatesPost(LinkUpdateRequest request) {
        notifier.notifyAll(request);
        return ResponseEntity.ok().build();
    }
}
