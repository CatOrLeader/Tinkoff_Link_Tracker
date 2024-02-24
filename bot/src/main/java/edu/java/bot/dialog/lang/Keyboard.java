package edu.java.bot.dialog.lang;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.utils.BotResponsesUtils;
import jakarta.validation.constraints.NotNull;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class Keyboard {
    private static final int MAX_LINK_LENGTH = 32;
    private static final String CALLBACK_INFO = "info %s";
    private static final String CALLBACK_REMOVE = "remove %s";
    private final BotAnswersProvider answersProvider;

    @Autowired
    public Keyboard(@NotNull BotAnswersProvider answersProvider) {
        this.answersProvider = answersProvider;
    }

    public @NotNull ReplyKeyboardMarkup mainMenu(@NotNull Locale userLocale) {
        return new ReplyKeyboardMarkup(
            new KeyboardButton(answersProvider.newResBtn(userLocale)),
            new KeyboardButton(answersProvider.remResBtn(userLocale))
        ).addRow(new KeyboardButton(answersProvider.listResBtn(userLocale))).resizeKeyboard(true);
    }

    public @NotNull InlineKeyboardMarkup userLinks(@NotNull Set<Link> links) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        for (Link link : links) {
            markup.addRow(
                new InlineKeyboardButton(BotResponsesUtils.decorateLink(link.url(), MAX_LINK_LENGTH))
                    .callbackData(String.format(
                        CALLBACK_INFO,
                        link.hashCode()
                    ))
                    .url(link.url())
            );
        }

        return markup;
    }

    public @NotNull InlineKeyboardMarkup userLinksWithRemoveButton(
        @NotNull Locale userLocale,
        @NotNull Set<Link> links
    ) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        for (Link link : links) {
            markup.addRow(
                new InlineKeyboardButton(BotResponsesUtils.decorateLink(link.url(), MAX_LINK_LENGTH))
                    .callbackData(String.format(
                        CALLBACK_INFO,
                        link.hashCode()
                    ))
                    .url(link.url()),
                new InlineKeyboardButton(answersProvider.removeBtn(userLocale))
                    .callbackData(String.format(CALLBACK_REMOVE, link.hashCode()))
            );
        }

        return markup;
    }
}
