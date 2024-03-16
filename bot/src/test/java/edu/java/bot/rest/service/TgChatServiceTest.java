package edu.java.bot.rest.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.bot.configuration.ClientConfiguration;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import org.jose4j.jwk.Use;
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
    private static String GET_CHAT_ANSWER;
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
        Path getChatAnswerJson = Path.of(resourceFolder.toAbsolutePath().toString(), "get_chat.json");

        API_ERROR = Files.readString(apiErrorJson);
        GET_CHAT_ANSWER = Files.readString(getChatAnswerJson);
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

        assertThat(service.registerNewChat(1L)).isNull();
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
            .isThrownBy(() -> service.registerNewChat(1L));
    }

    @Test
    void givenGetRequest_whenCorrectlyAssembled_thenAppropriateHandling() {
        server.stubFor(
            WireMock.get(WireMock.urlPathMatching("/tg-chat/[0-9]+"))
                .willReturn(
                    WireMock.jsonResponse(GET_CHAT_ANSWER, HttpStatus.OK.value())
                )
        );

        UserData expectedValue = new UserData(1L, BotState.MAIN_MENU, Locale.ENGLISH);
        var actualValue = service.getChat(1L);

        assertThat(actualValue).contains(expectedValue);
    }

    @Test
    void givenGetRequest_whenIncorrectlyAssembled_thenException() {
        server.stubFor(
            WireMock.get(WireMock.urlMatching("/tg-chat/[0-9]+"))
                .willReturn(
                    WireMock.jsonResponse(API_ERROR, HttpStatus.BAD_REQUEST.value())
                )
        );

        assertThatExceptionOfType(HttpClientErrorException.class)
            .isThrownBy(() -> service.getChat(1L));
    }

    @Test
    void givenPostRequestWithUpdate_whenCorrectlyAssembled_thenAppropriateHandling() {
        UserData userData = new UserData(1L, BotState.MAIN_MENU, Locale.ENGLISH);
        server.stubFor(
            WireMock.post(WireMock.urlMatching("/tg-chat"))
                .willReturn(
                    WireMock.ok()
                )
        );


        assertThat(service.updateChat(userData)).isNull();
    }

    @Test
    void givenPostRequestWithUpdate_whenIncorrectlyAssembled_thenException() {
        UserData userData = new UserData(1L, BotState.MAIN_MENU, Locale.ENGLISH);
        server.stubFor(
            WireMock.post(WireMock.urlMatching("/tg-chat"))
                .willReturn(
                    WireMock.jsonResponse(API_ERROR, HttpStatus.BAD_REQUEST.value())
                )
        );


        assertThatExceptionOfType(HttpClientErrorException.class)
            .isThrownBy(() -> service.updateChat(userData));
    }

    @Test
    void givenDeleteRequest_whenCorrectlyAssembled_thenAppropriateHandling() {
        server.stubFor(
            WireMock.delete(WireMock.urlMatching("/tg-chat/[0-9]+"))
                .willReturn(
                    WireMock.ok()
                )
        );

        assertThat(service.deleteChat(1L)).isNull();
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
            .isThrownBy(() -> service.deleteChat(1L));
    }
}
