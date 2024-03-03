package edu.java.bot.rest.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.bot.configuration.ClientConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(classes = {TgChatService.class, ClientConfiguration.class})
public class TgChatServiceTest {
    @RegisterExtension
    private static final WireMockExtension server = WireMockExtension.newInstance()
        .options(WireMockConfiguration.options().dynamicPort())
        .build();
    private static String API_ERROR;
    @Autowired
    private TgChatService service;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.clients.scrapper-url", server::baseUrl);
    }

    @BeforeAll
    static void tearUp() throws IOException {
        Path resourceFolder = Path.of("src", "test", "resources", "edu", "java", "bot", "rest", "service");
        Path apiErrorJson = Path.of(resourceFolder.toAbsolutePath().toString(), "api_error.json");

        API_ERROR = Files.readString(apiErrorJson);
    }

    @AfterEach
    void afterEach() {
        server.resetAll();
    }

    @Test
    void givenPostRequest_whenCorrectlyAssembled_thenAppropriateHandling() {
        server.stubFor(
            WireMock.post(WireMock.urlMatching("/tg-chat/[0-9]+"))
                .willReturn(
                    WireMock.ok()
                )
        );

        assertThat(service.registerNewChat(1L).block()).isNull();
    }

    @Test
    void givenPostRequest_whenIncorrectlyAssembled_thenException() {
        server.stubFor(
            WireMock.post(WireMock.urlMatching("/tg-chat/[0-9]+"))
                .willReturn(
                    WireMock.jsonResponse(API_ERROR, HttpStatus.BAD_REQUEST.value())
                )
        );

        assertThatExceptionOfType(HttpClientErrorException.class)
            .isThrownBy(() -> service.registerNewChat(1L).block());
    }

    @Test
    void givenDeleteRequest_whenCorrectlyAssembled_thenAppropriateHandling() {
        server.stubFor(
            WireMock.delete(WireMock.urlMatching("/tg-chat/[0-9]+"))
                .willReturn(
                    WireMock.ok()
                )
        );

        assertThat(service.deleteChat(1L).block()).isNull();
    }

    @Test
    void givenDeleteRequest_whenIncorrectlyAssembled_thenException() {
        server.stubFor(
            WireMock.delete(WireMock.urlMatching("/tg-chat/[0-9]+"))
                .willReturn(
                    WireMock.jsonResponse(API_ERROR, HttpStatus.BAD_REQUEST.value())
                )
        );

        assertThatExceptionOfType(HttpClientErrorException.class)
            .isThrownBy(() -> service.deleteChat(1L).block());
    }
}
