package edu.java.scrapper.rest.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.scrapper.rest.model.LinkUpdate;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UpdatesServiceTest {
    @RegisterExtension
    private static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.options().dynamicPort())
        .build();
    @Autowired
    private UpdatesService service;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.clients.bot-url", wireMockServer::baseUrl);
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void givenRequest_whenAllDataIsCorrect_then200() {
        wireMockServer.stubFor(
            WireMock.post(
                "/updates"
            ).willReturn(
                WireMock.ok()
            )
        );
        LinkUpdate update = new LinkUpdate(
            0L,
            URI.create("https://github.com"),
            "dont know",
            List.of(0L)
        );

        var response = service.postLinkUpdate(update);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.hasBody()).isFalse();
    }
}
