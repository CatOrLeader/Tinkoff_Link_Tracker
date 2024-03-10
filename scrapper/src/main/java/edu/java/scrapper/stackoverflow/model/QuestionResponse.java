package edu.java.scrapper.stackoverflow.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.scrapper.stackoverflow.deserializers.QuestionJsonDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(using = QuestionJsonDeserializer.class)
public class QuestionResponse {
    @NotNull private URI url;
    @NotBlank private String title;
    @NotNull private OffsetDateTime lastActivityDate;
}
