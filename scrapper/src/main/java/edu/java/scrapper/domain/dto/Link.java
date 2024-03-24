package edu.java.scrapper.domain.dto;

import edu.java.scrapper.github.model.IssueResponse;
import edu.java.scrapper.github.model.PullResponse;
import edu.java.scrapper.stackoverflow.model.QuestionResponse;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import static edu.java.scrapper.utils.DateTimeUtils.OFFSET_HOURS;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = {"id", "uri"})
public class Link {
    private static final int MAX_STRING_SIZE = 128;
    private long id;
    private @NotNull URI uri;
    private String description;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private String createdBy;
    private String updatedBy;
    private String title;
    private String etag;
    private OffsetDateTime lastCheckedAt;
    private ResponseType type;

    public Link(@NotNull IssueResponse response) {
        this.uri = response.htmlUrl();
        this.description = response.body().substring(0, Math.min(response.body().length(), MAX_STRING_SIZE));
        this.title = response.title().substring(0, Math.min(response.title().length(), MAX_STRING_SIZE));
        this.createdAt = response.createdAt();
        this.updatedAt = response.updatedAt();
        this.createdBy = response.user().login();
        this.type = IssueResponse.TYPE;
    }

    public Link(@NotNull PullResponse response) {
        this.uri = response.htmlUrl();
        this.description = response.body().substring(0, Math.min(response.body().length(), MAX_STRING_SIZE));
        this.title = response.title().substring(0, Math.min(response.title().length(), MAX_STRING_SIZE));
        this.createdAt = response.createdAt();
        this.updatedAt = response.updatedAt();
        this.createdBy = response.user().login();
        this.type = PullResponse.TYPE;
    }

    public Link(@NotNull QuestionResponse response) {
        this.uri = response.url();
        this.title = response.title().substring(0, Math.min(response.title().length(), MAX_STRING_SIZE));
        this.updatedAt = response.lastActivityDate();
        this.type = QuestionResponse.TYPE;
    }

    public Link(@NotNull edu.java.scrapper.domain.jooq.tables.pojos.Link link) {
        this(link.getId(), URI.create(link.getUri()), link.getDescription(),
            link.getCreatedAt() != null ? link.getCreatedAt().atOffset(ZoneOffset.ofHours(OFFSET_HOURS)) : null,
            link.getUpdatedAt() != null ? link.getUpdatedAt().atOffset(ZoneOffset.ofHours(OFFSET_HOURS)) : null,
            link.getCreatedBy(), link.getUpdatedBy(), link.getTitle(), link.getEtag(),
            link.getLastCheckedAt() != null ? link.getLastCheckedAt().atOffset(ZoneOffset.ofHours(OFFSET_HOURS)) : null,
            link.getType() != null ? ResponseType.valueOf(link.getType()) : null
        );
    }
}
