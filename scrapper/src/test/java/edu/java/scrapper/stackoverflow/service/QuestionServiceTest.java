package edu.java.scrapper.stackoverflow.service;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import edu.java.configuration.ClientConfiguration;
import edu.java.stackoverflow.model.QuestionResponse;
import edu.java.stackoverflow.service.QuestionService;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {QuestionService.class, ClientConfiguration.class})
public class QuestionServiceTest {
    @RegisterExtension
    private static final WireMockExtension wireMockServer = WireMockExtension.newInstance()
        .options(WireMockConfiguration.options().dynamicPort())
        .build();
    private static String JSON_BODY;
    @Autowired
    private QuestionService service;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("app.clients.stackoverflow-url", wireMockServer::baseUrl);
    }

    @BeforeAll
    static void tearUp() throws IOException {
        Path resourceDir = Paths.get("src", "test", "resources", "edu", "java", "stackoverflow");
        Path questionAnswerFile = Paths.get(resourceDir.toAbsolutePath().toString(), "question_answer.json");

        JSON_BODY = Files.readString(questionAnswerFile);
    }

    @AfterEach
    void afterEach() {
        wireMockServer.resetAll();
    }

    @Test
    void givenDataAboutQuestion_whenRequestSend_thenCorrectlyDTORetrieved() {
        wireMockServer.stubFor(
            WireMock.get(
                WireMock.urlPathMatching("/2.3/questions/.*")
            ).willReturn(
                WireMock.okJson(JSON_BODY)
            )
        );

        QuestionResponse expectedValue = new QuestionResponse(
            "https://stackoverflow.com/questions/1495666/how-can-i-define-a-class-in-python",
            "How can I define a class in Python?",
            OffsetDateTime.parse("2020-03-16T09:45:48+00:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        );
        QuestionResponse actualValue = service.getQuestionById("1234");

        assertThat(actualValue).isEqualTo(expectedValue);
    }
}
