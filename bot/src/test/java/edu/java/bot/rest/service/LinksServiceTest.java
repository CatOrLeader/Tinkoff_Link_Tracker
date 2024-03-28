package edu.java.bot.rest.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.bot.configuration.ClientConfiguration;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.rest.model.ApiErrorResponse;
import edu.java.bot.rest.model.LinkResponse;
import edu.java.bot.rest.model.ListLinksResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.web.client.HttpClientErrorException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest(classes = {LinksService.class, ClientConfiguration.class})
public class LinksServiceTest {
    @RegisterExtension
    private static final WireMockExtension server = WireMockExtension.newInstance()
        .options(WireMockConfiguration.options().dynamicPort())
        .build();
    private static String GET_LINKS;
    private static ListLinksResponse GET_LINKS_RESPONSE;
    private static String POST_LINKS;
    private static LinkResponse POST_LINKS_RESPONSE;
    private static String DELETE_LINKS;
    private static LinkResponse DELETE_LINKS_RESPONSE;
    private static String API_ERROR;
    private static ApiErrorResponse API_ERROR_RESPONSE;
    @Autowired
    private LinksService service;

    @BeforeAll
    static void tearUp() throws IOException {
        Path resourceFolder = Path.of("src", "test", "resources", "edu", "java", "bot", "rest", "service");
        Path getLinksJson = Path.of(resourceFolder.toAbsolutePath().toString(), "get_links.json");
        Path postLinksJson = Path.of(resourceFolder.toAbsolutePath().toString(), "post_links.json");
        Path deleteLinksJson = Path.of(resourceFolder.toAbsolutePath().toString(), "delete_links.json");
        Path apiErrorJson = Path.of(resourceFolder.toAbsolutePath().toString(), "api_error.json");

        ObjectMapper mapper = new ObjectMapper();

        GET_LINKS = Files.readString(getLinksJson);
        GET_LINKS_RESPONSE = mapper.readValue(GET_LINKS, ListLinksResponse.class);

        POST_LINKS = Files.readString(postLinksJson);
        POST_LINKS_RESPONSE = mapper.readValue(POST_LINKS, LinkResponse.class);

        DELETE_LINKS = Files.readString(deleteLinksJson);
        DELETE_LINKS_RESPONSE = mapper.readValue(DELETE_LINKS, LinkResponse.class);

        API_ERROR = Files.readString(apiErrorJson);
        API_ERROR_RESPONSE = mapper.readValue(API_ERROR, ApiErrorResponse.class);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.clients.scrapper-url", server::baseUrl);
    }

    @AfterEach
    void afterEach() {
        server.resetAll();
    }

    @Test
    void givenGetLinkRequest_whenSuccessResponse_thenCorrectHandling() {
        server.stubFor(
            WireMock.get("/links")
                .withHeader("Tg-Chat-Id", WireMock.matching("[0-9]+"))
                .willReturn(
                    WireMock.okJson(GET_LINKS)
                )
        );

        var expectedResponse = GET_LINKS_RESPONSE.links().stream()
            .map(Link::new)
            .collect(Collectors.toList());
        var actualResponse = service.getLinks(1L);

        assertThat(actualResponse).contains(expectedResponse);
    }

    @Test
    void givenGetLinkIncorrectRequest_whenResponse_thenException() {
        server.stubFor(
            WireMock.get("/links")
                .withHeader("Tg-Chat-Id", WireMock.matching("[0-9]+"))
                .willReturn(
                    WireMock.jsonResponse(API_ERROR, HttpStatus.BAD_REQUEST.value())
                )
        );

        HttpClientErrorException expectedException = HttpClientErrorException.create(
            HttpStatusCode.valueOf(Integer.parseInt(API_ERROR_RESPONSE.code())),
            API_ERROR_RESPONSE.description(),
            null, null, null
        );

        assertThatExceptionOfType(expectedException.getClass())
            .isThrownBy(() -> service.getLinks(1L));
    }

    @Test
    void givenPostLinkRequest_whenSuccessResponse_thenCorrectHandling() {
        server.stubFor(
            WireMock.post("/links")
                .withHeader("Tg-Chat-Id", WireMock.matching("[0-9]+"))
                .withRequestBody(WireMock.not(WireMock.absent()))
                .willReturn(
                    WireMock.okJson(POST_LINKS)
                )
        );
        Link link = new Link(URI.create("https://github.com"));

        var expectedResponse = new Link(POST_LINKS_RESPONSE);
        var actualResponse = service.postLink(1L, link);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenPostLinkRequest_whenSuccessResponse_thenException() {
        server.stubFor(
            WireMock.post("/links")
                .withHeader("Tg-Chat-Id", WireMock.matching("[0-9]+"))
                .withRequestBody(WireMock.not(WireMock.absent()))
                .willReturn(
                    WireMock.jsonResponse(API_ERROR_RESPONSE, HttpStatus.BAD_REQUEST.value())
                )
        );
        Link link = new Link(URI.create("https://github.com"));

        HttpClientErrorException expectedException = HttpClientErrorException.create(
            HttpStatusCode.valueOf(Integer.parseInt(API_ERROR_RESPONSE.code())),
            API_ERROR_RESPONSE.description(),
            null, null, null
        );

        assertThatExceptionOfType(expectedException.getClass())
            .isThrownBy(() -> service.postLink(1L, link));
    }

    @Test
    void givenDeleteLinkRequest_whenSuccessResponse_thenCorrectHandling() {
        server.stubFor(
            WireMock.delete("/links")
                .withHeader("Tg-Chat-Id", WireMock.matching("[0-9]+"))
                .withRequestBody(WireMock.not(WireMock.absent()))
                .willReturn(
                    WireMock.okJson(DELETE_LINKS)
                )
        );
        Link link = new Link(URI.create("https://github.com"));

        var expectedResponse = new Link(DELETE_LINKS_RESPONSE);
        var actualResponse = service.deleteLink(1L, link.getId());

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void givenDeleteLinkRequest_whenSuccessResponse_thenException() {
        server.stubFor(
            WireMock.delete("/links")
                .withHeader("Tg-Chat-Id", WireMock.matching("[0-9]+"))
                .withRequestBody(WireMock.not(WireMock.absent()))
                .willReturn(
                    WireMock.jsonResponse(API_ERROR_RESPONSE, HttpStatus.BAD_REQUEST.value())
                )
        );
        Link link = new Link(URI.create("https://github.com"));

        HttpClientErrorException expectedException = HttpClientErrorException.create(
            HttpStatusCode.valueOf(Integer.parseInt(API_ERROR_RESPONSE.code())),
            API_ERROR_RESPONSE.description(),
            null, null, null
        );

        assertThatExceptionOfType(expectedException.getClass())
            .isThrownBy(() -> service.deleteLink(1L, link.getId()));
    }
}
