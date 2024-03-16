package edu.java.bot.utils;

import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import jakarta.validation.constraints.NotEmpty;
import java.util.Arrays;
import org.jetbrains.annotations.NotNull;

public final class BotResponsesUtils {
    private BotResponsesUtils() {
    }

    public static @NotNull BaseRequest[] concatenate(@NotEmpty BaseRequest[]... responses) {
        int responsesCount = responses.length;
        if (responsesCount == 1) {
            return responses[0];
        }

        int overallLength = 0;
        for (var arr : responses) {
            overallLength += arr.length;
        }

        int currentLength = responses[0].length;
        BaseRequest[] responsesAll = Arrays.copyOf(responses[0], overallLength);
        for (int i = 1; i < responsesCount; i++) {
            int length = responses[i].length;

            System.arraycopy(responses[i], 0, responsesAll, currentLength, length);
            currentLength += length;
        }

        return responsesAll;
    }

    public static int extractLinkCodeFromCallbackQuery(@NotNull CallbackQuery query) {
        return Integer.parseInt(query.data().strip().substring("cancel".length() + 1));
    }

    public static @NotNull String decorateLink(@NotNull String link, int maxLength) {
        if (maxLength < 1) {
            throw new IndexOutOfBoundsException();
        }

        return link.length() > maxLength
            ? link.substring(0, maxLength) + "..."
            : link;
    }
}
