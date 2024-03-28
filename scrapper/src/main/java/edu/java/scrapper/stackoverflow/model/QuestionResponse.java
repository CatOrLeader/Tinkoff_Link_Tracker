package edu.java.scrapper.stackoverflow.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.scrapper.domain.dto.ResponseType;
import edu.java.scrapper.stackoverflow.deserializers.QuestionJsonDeserializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonDeserialize(using = QuestionJsonDeserializer.class)
public record QuestionResponse(@NotNull URI url,
                               @NotBlank String title,
                               @NotNull OffsetDateTime lastActivityDate) {
    public static final ResponseType TYPE = ResponseType.SFO_QUESTION;
}
