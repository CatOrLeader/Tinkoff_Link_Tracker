package edu.java.bot.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record LinkUpdateRequest(@Min(0) long id, @NotNull URI url, @NotBlank String description,
                                @NotEmpty List<GetChatResponse> tgChatIds) {
}
