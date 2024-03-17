package edu.java.scrapper.github.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record PullResponse(@NotNull URI url,
                           @NotNull URI htmlUrl,
                           @NotBlank String state,
                           @NotNull String title,
                           @NotNull OffsetDateTime updatedAt,
                           OffsetDateTime closedAt,
                           boolean merged) {
}
