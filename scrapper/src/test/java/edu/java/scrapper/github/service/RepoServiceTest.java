package edu.java.scrapper.github.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.scrapper.configuration.ClientConfiguration;
import edu.java.scrapper.github.model.RepoResponse;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {RepoService.class, ClientConfiguration.class})
public class RepoServiceTest {
    @RegisterExtension
    private static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(wireMockConfig().dynamicPort())
        .build();
    private static String JSON_BODY;
    @Autowired
    private RepoService service;

    @DynamicPropertySource
    static void configureProperties(
        DynamicPropertyRegistry registry
    ) {
        registry.add("app.clients.github-url", wireMockServer::baseUrl);
    }

    @BeforeAll
    static void tearUp() throws IOException {
        Path resourcesDir = Paths.get("src", "test", "resources", "edu", "java", "scrapper", "github");
        Path repoAnswerFile = Paths.get(resourcesDir.toAbsolutePath().toString(), "repo_answer.json");

        JSON_BODY = Files.readString(repoAnswerFile);
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void givenDataAboutRepo_whenRequestSend_thenCorrectlyDTORetrieved() {
        wireMockServer.stubFor(
            WireMock.get(
                WireMock.urlPathMatching("/repos/\\w+/\\w+/pulls/\\d+")
            ).willReturn(
                WireMock.okJson(JSON_BODY)
            )
        );

        RepoResponse expectedValue = new RepoResponse(
            URI.create("https://api.github.com/repos/CatOrLeader/Tinkoff_Link_Tracker/pulls/2"),
            "HW #1",
            OffsetDateTime.parse("2024-02-18T19:21:18Z")
        );
        RepoResponse actualValue = service.getRepoByOwnerNameNumber("CatOrLeader", "Tinkoff_Link_Tracker", 2);

        assertThat(actualValue).isEqualTo(expectedValue);
    }
}

