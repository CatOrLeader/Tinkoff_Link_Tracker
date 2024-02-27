package edu.java.scrapper.rest.api;

import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = TgChatController.class)
@AutoConfigureWebTestClient
public class TgChatControllerTest {
    @Autowired
    private WebTestClient client;

    @Test
    void givenCorrectGetRequest_whenIdPresented_thenResponseStatusIsOk() {
        client.post()
            .uri("/tg-chat/{id}", Map.of("id", 123))
            .exchange().expectStatus().isOk().expectBody().isEmpty();
    }

    @Test
    void givenCorrectGetRequest_whenIdIsNotPresented_thenResponseStatusIs400() {
        client.post()
            .uri("/tg-chat/inc")
            .exchange().expectStatus().isBadRequest();
    }

    @Test
    void givenCorrectDelRequest_whenIdPresented_thenResponseStatusIsOk() {
        client.delete()
            .uri("/tg-chat/{id}", Map.of("id", 123))
            .exchange().expectStatus().isOk();
    }

    @Test
    void givenCorrectDelRequest_whenIdIsNotPresented_thenResponseStatusIs400() {
        client.delete()
            .uri("/tg-chat/inc")
            .exchange().expectStatus().isBadRequest();
    }
}
