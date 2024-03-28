package edu.java.scrapper.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.scrapper.domain.dto.Link;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record LinkResponse(long id, @NotNull URI url) {
    public LinkResponse(@NotNull Link link) {
        this(link.getId(), link.getUri());
    }
}
