package edu.java.bot.rest.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import edu.java.bot.dialog.data.UserData;
import jakarta.validation.constraints.NotBlank;
import org.jetbrains.annotations.NotNull;

@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public record UpdateChatRequest(long userId, @NotBlank String botState, @NotBlank String locale) {
    public UpdateChatRequest(@NotNull UserData userData) {
        this(userData.getUserId(), userData.getDialogState().name(), userData.getLocale().toLanguageTag());
    }
}
