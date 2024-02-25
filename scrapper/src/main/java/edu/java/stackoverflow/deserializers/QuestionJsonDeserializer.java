package edu.java.stackoverflow.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import edu.java.stackoverflow.model.QuestionResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class QuestionJsonDeserializer extends JsonDeserializer<QuestionResponse> {
    @Override
    public QuestionResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
        TreeNode json = jsonParser.getCodec().readTree(jsonParser).get("items").get(0);
        return new QuestionResponse(
            json.get("link").toString().replaceAll("\"", ""),
            json.get("title").toString().replaceAll("\"", ""),
            OffsetDateTime.ofInstant(
                Instant.ofEpochSecond(Long.parseLong(json.get("last_activity_date").toString())),
                ZoneOffset.UTC
            )
        );
    }
}
