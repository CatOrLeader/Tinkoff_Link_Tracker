package edu.java.scrapper.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.scrapper.domain.dto.Link;
import edu.java.scrapper.domain.dto.TgChat;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record LinkUpdateRequest(@Min(0) long id, @NotNull URI url, @NotBlank String description,
                                @NotEmpty List<GetChatResponse> tgChatIds) {
    public LinkUpdateRequest(@NotNull Link link, @NotEmpty Collection<TgChat> tgChats) {
        this(link.getId(), link.getUri(), link.getDescription(),
            tgChats.stream().map(GetChatResponse::new).collect(Collectors.toList())
        );
    }
}
