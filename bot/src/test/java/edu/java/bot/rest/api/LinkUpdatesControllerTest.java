package edu.java.bot.rest.api;

import edu.java.bot.dialog.service.LinkUpdateNotifier;
import edu.java.bot.rest.model.GetChatResponse;
import edu.java.bot.rest.model.LinkUpdateRequest;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@WebFluxTest(controllers = LinkUpdatesController.class)
@MockBean(LinkUpdateNotifier.class)
@AutoConfigureWebTestClient
public class LinkUpdatesControllerTest {
    @Autowired
    private WebTestClient client;

    @Test
    void givenRequest_whenBodyIsCorrect_thenResponseStatusIs200() {
        LinkUpdateRequest request = new LinkUpdateRequest(
            1, URI.create("https://github.com"), "lol",
            List.of(new GetChatResponse(1L, "MAIN_MENU", "EN"))
        );

        client.post()
            .uri("/updates").accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange().expectStatus().isOk();
    }

    @Test
    void givenRequest_whenBodyIsIncorrect_thenResponseStatusIs400() {
        LinkUpdateRequest request = Mockito.mock(LinkUpdateRequest.class);

        client.post()
            .uri("/updates").accept(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .exchange().expectStatus().isBadRequest();
    }
}
