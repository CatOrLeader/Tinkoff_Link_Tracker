package edu.java.scrapper.rest.service;

import edu.java.scrapper.rest.model.LinkUpdateRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class UpdatesService {
    private final WebClient botWebClient;

    public ResponseEntity<Void> postLinkUpdate(@NotNull LinkUpdateRequest update) {
        return botWebClient.post()
            .uri("/updates")
            .bodyValue(update)
            .retrieve()
            .toBodilessEntity().block();
    }
}
