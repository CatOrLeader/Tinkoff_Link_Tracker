package edu.java.bot.dialog.data;

import edu.java.bot.rest.model.GetChatResponse;
import java.util.Locale;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public final class UserData {
    private final long userId;
    private @NotNull BotState dialogState;
    private @NotNull Locale locale;

    public UserData(@NotNull GetChatResponse response) {
        this(
            response.userId(),
            BotState.valueOf(response.dialogState()),
            Locale.forLanguageTag(response.languageTag())
        );
    }

    public boolean isRegistered() {
        return !dialogState.equals(BotState.UNINITIALIZED);
    }
}
