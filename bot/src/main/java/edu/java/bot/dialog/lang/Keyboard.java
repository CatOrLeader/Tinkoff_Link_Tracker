package edu.java.bot.dialog.lang;

import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.utils.BotResponsesUtils;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Locale;
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

    public @NotNull ReplyKeyboardMarkup goBack(@NotNull Locale userLocale) {
        return new ReplyKeyboardMarkup(
            new KeyboardButton(answersProvider.goBackBtn(userLocale))
        ).resizeKeyboard(true);
    }

    public @NotNull InlineKeyboardMarkup userLinks(@NotNull List<Link> links) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        for (Link link : links) {
            String uriStr = String.valueOf(link.getUrl());

            markup.addRow(
                new InlineKeyboardButton(BotResponsesUtils.decorateLink(uriStr, MAX_LINK_LENGTH))
                    .callbackData(String.format(
                        CALLBACK_INFO,
                        link.getId()
                    ))
                    .url(uriStr)
            );
        }

        return markup;
    }

    public @NotNull InlineKeyboardMarkup userLinksWithRemoveButton(
        @NotNull Locale userLocale,
        @NotNull List<Link> links
    ) {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        for (Link link : links) {
            String uriStr = String.valueOf(link.getUrl());
            long id = link.getId();

            markup.addRow(
                new InlineKeyboardButton(BotResponsesUtils.decorateLink(uriStr, MAX_LINK_LENGTH))
                    .callbackData(String.format(
                        CALLBACK_INFO,
                        id
                    ))
                    .url(uriStr),
                new InlineKeyboardButton(answersProvider.removeBtn(userLocale))
                    .callbackData(String.format(CALLBACK_REMOVE, id))
            );
        }

        return markup;
    }
}
