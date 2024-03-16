package edu.java.scrapper.github.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class IssueResponse {
    @NotNull private URI url;
    @NotNull private URI htmlUrl;
    @NotBlank private String title;
    @NotBlank private String state;
    @NotNull private OffsetDateTime updatedAt;
    private OffsetDateTime closedAt;
    private String closedBy;
}
