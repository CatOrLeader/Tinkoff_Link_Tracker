package edu.java.scrapper.stackoverflow.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.scrapper.stackoverflow.deserializers.QuestionJsonDeserializer;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(using = QuestionJsonDeserializer.class)
public class QuestionResponse {
    private String url;
    private String title;
    private OffsetDateTime lastActivityDate;
}
