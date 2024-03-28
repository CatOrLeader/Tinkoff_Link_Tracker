package edu.java.scrapper.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record ListLinksResponse(@NotNull List<LinkResponse> links, @Min(0) int size) {
    public ListLinksResponse(@NotNull List<LinkResponse> linkResponses) {
        this(linkResponses, linkResponses.size());
    }
}
