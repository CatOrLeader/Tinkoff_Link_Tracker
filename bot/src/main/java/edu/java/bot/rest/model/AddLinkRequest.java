package edu.java.bot.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.bot.dialog.data.Link;
import jakarta.validation.constraints.NotNull;
import java.net.URI;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record AddLinkRequest(@NotNull URI link) {
    public AddLinkRequest(@NotNull Link link) {
        this(link.getUrl());
    }
}
