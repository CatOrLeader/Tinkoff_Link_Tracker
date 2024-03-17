package edu.java.scrapper.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotEmpty;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record GetChatResponse(long userId, @NotEmpty String dialogState, @NotEmpty String languageTag) {
}
