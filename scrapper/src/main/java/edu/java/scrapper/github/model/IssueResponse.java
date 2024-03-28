package edu.java.scrapper.github.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.scrapper.domain.dto.ResponseType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record IssueResponse(@NotNull URI url,
                            @NotBlank String body,
                            @NotNull URI htmlUrl,
                            @NotBlank String title,
                            @NotBlank String state,
                            @NotNull OffsetDateTime createdAt,
                            @NotNull OffsetDateTime updatedAt,
                            @NotNull User user) {
    public static final ResponseType TYPE = ResponseType.GITHUB_ISSUE;
}
