package edu.java.scrapper.github.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import static edu.java.scrapper.utils.DateTimeUtils.VERBOSE_DATETIME;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record IssueEventResponse(
    @NotNull URI url,
    @NotBlank String event,
    @NotNull OffsetDateTime createdAt,
    @JsonProperty("actor")
    @NotNull User user
) {
    @Override
    public String toString() {
        return "- User: " + user.login() + "\n- Event: " + event + "\n- Created at: "
               + createdAt.format(VERBOSE_DATETIME);
    }
}
