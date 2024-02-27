package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.data.UserDataStorage;
import edu.java.bot.dialog.data.UserLinksTracker;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.dialog.lang.Keyboard;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UntrackHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final Keyboard keyboard;
    private final UserDataStorage userDataStorage;
    private final UserLinksTracker linksTracker;

    @Autowired
    public UntrackHandler(
        @NotNull BotAnswersProvider answersProvider,
        @NotNull Keyboard keyboard,
        @NotNull UserDataStorage userDataStorage,
        @NotNull UserLinksTracker linksTracker
    ) {
        this.answersProvider = answersProvider;
        this.keyboard = keyboard;
        this.userDataStorage = userDataStorage;
        this.linksTracker = linksTracker;
    }

    @Override
    public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (isInappropriateRequest(update, userData)) {
            return Optional.empty();
        }

        var response = constructTemplateResponse(update, userData);
        setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        var userId = userData.getUserID();
        Locale userLocale = userData.getLocale();
        var links = linksTracker.getUserLinks(userId);

        if (links.isEmpty()) {
            return new SendMessage[] {new SendMessage(
                userId,
                answersProvider.noResYet(userLocale)
            ).parseMode(ParseMode.Markdown)};
        } else {
            return new SendMessage[] {
                new SendMessage(
                    userId,
                    answersProvider.resUnregisterWaiting(userLocale)
                ).parseMode(ParseMode.Markdown),
                new SendMessage(
                    userId,
                    answersProvider.trackedResources(userLocale)
                ).replyMarkup(keyboard.userLinksWithRemoveButton(userLocale, links))
                    .parseMode(ParseMode.Markdown)
            };
        }
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        if (responses.length > 1) { // IT is truly a nightmare, BTW I for now cannot imagine something else
            userDataStorage.setUserState(userData, BotState.RES_UNTRACK_WAITING);
        }
    }

    private boolean isInappropriateRequest(Update update, UserData userData) {
        return !MessagesApprovalUtils.equalsTextMessageContent(update.message(), "/untrack")
               || !userData.isRegistered();
    }
}
