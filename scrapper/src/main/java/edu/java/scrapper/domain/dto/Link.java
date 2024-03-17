package edu.java.scrapper.domain.dto;

import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Link {
    private long id;
    private @NotNull URI uri;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String updateDescription;
    private String etag;
    private OffsetDateTime lastCheckedAt;
    private ResponseType type;
}
