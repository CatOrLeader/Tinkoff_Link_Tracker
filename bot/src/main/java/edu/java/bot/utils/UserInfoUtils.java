package edu.java.bot.utils;

import com.pengrad.telegrambot.model.Update;
import java.util.Optional;
import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;

public final class UserInfoUtils {
    private UserInfoUtils() {
    }

    @NotNull
    public static Optional<Long> extractUserId(@NotNull Update update) {
        try {
            if (update.message() != null) {
                return update.message().from().id().describeConstable();
            }

            if (update.callbackQuery().from().id() != null) {
                return update.callbackQuery().from().id().describeConstable();
            }
        } catch (Exception any) {
            LogManager.getLogger().error(any);
        }
        return Optional.empty();
    }
}
