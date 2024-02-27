package edu.java.bot.dialog.data;

import java.util.Locale;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Getter
@Setter(AccessLevel.PACKAGE)
public final class UserData {
    private final long userID;
    private @NotNull BotState dialogState;
    private @NotNull Locale locale;

    public static @NotNull UserData constructInitialFromId(long userId) {
        return new UserData(
            userId,
            BotState.UNINITIALIZED,
            Locale.ENGLISH
        );
    }

    public boolean isRegistered() {
        return !dialogState.equals(BotState.UNINITIALIZED);
    }
}
