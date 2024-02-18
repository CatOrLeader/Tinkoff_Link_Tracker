package edu.java.bot.utils;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.jetbrains.annotations.NotNull;

public final class MessagesApprovalUtils {
    private MessagesApprovalUtils() {
    }

    public static boolean equalsTextMessageContent(Message received, @NotNull String expectedContent) {
        return isMsgExists(received) && received.text().strip().equals(expectedContent);
    }

    public static boolean isMsgExists(Message received) {
        return received != null && received.text() != null;
    }

    public static boolean isCallbackQueryExists(@NotNull Update update) {
        return update.callbackQuery() != null && update.callbackQuery().data() != null
               && !update.callbackQuery().data().isBlank();
    }

    public static boolean isCorrectResource(Message received) {
        final String regexForLink = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        return isMsgExists(received) && received.text().strip().matches(regexForLink);
    }
}
