package edu.java.scrapper.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.scrapper.domain.dto.TgChat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record GetChatResponse(long userId, @NotEmpty String dialogState, @NotEmpty String languageTag) {
    public GetChatResponse(@NotNull TgChat tgChat) {
        this(tgChat.getId(), tgChat.getDialogState(), tgChat.getLanguageTag());
    }
}
