package edu.java.scrapper.stackoverflow.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.java.scrapper.stackoverflow.model.QuestionResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class QuestionJsonDeserializerTest {
    private static String JSON_BODY;

    @BeforeAll
    static void tearUp() throws IOException {
        Path resourceDir = Paths.get("src", "test", "resources", "edu", "java", "scrapper", "stackoverflow");
        Path questionAnswerFile = Paths.get(resourceDir.toAbsolutePath().toString(), "question_answer.json");

        JSON_BODY = Files.readString(questionAnswerFile);
    }

    @Test
    void givenJsonContent_whenDeserialized_thenCorrectDTO() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser(JSON_BODY);
        DeserializationContext context = mapper.getDeserializationContext();

        QuestionResponse expectedValue = new QuestionResponse(
            "https://stackoverflow.com/questions/1495666/how-can-i-define-a-class-in-python",
            "How can I define a class in Python?",
            OffsetDateTime.parse("2020-03-16T09:45:48+00:00", DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        );
        QuestionResponse actualValue = new QuestionJsonDeserializer().deserialize(parser, context);

        assertThat(actualValue).isEqualTo(expectedValue);
    }
}
