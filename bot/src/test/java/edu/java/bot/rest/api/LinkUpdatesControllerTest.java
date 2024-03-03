package edu.java.bot.rest.api;

import edu.java.bot.rest.model.LinkUpdateRequest;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = LinkUpdatesController.class)
@AutoConfigureWebTestClient
public class LinkUpdatesControllerTest {
    @Autowired
    private WebTestClient client;

    @Test
    void givenRequest_whenBodyIsCorrect_thenResponseStatusIs200() {
        LinkUpdateRequest request = new LinkUpdateRequest(
            1, URI.create("https://github.com"), "lol",
            List.of(1)
        );

        client.post()
            .uri("/updates").accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange().expectStatus().isOk();
    }

    @Test
    void givenRequest_whenBodyIsIncorrect_thenResponseStatusIs400() {
        LinkUpdateRequest request = new LinkUpdateRequest(
            -1, URI.create("https://github.com"), "lol",
            new ArrayList<>()
        );

        client.post()
            .uri("/updates").accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange().expectStatus().isBadRequest();
    }
}
