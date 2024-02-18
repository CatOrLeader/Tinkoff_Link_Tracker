package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.Link;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.data.UserDataStorage;
import edu.java.bot.dialog.data.UserLinksTracker;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.utils.BotResponsesUtils;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ResToTrackReceivedHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UpdateHandler menuHandler;
    private final UserDataStorage userDataStorage;
    private final UserLinksTracker linksTracker;

    @Autowired
    public ResToTrackReceivedHandler(
        @NotNull BotAnswersProvider answersProvider,
        @NotNull UpdateHandler menuHandler,
        @NotNull UserDataStorage userDataStorage,
        @NotNull UserLinksTracker linksTracker
    ) {
        this.answersProvider = answersProvider;
        this.menuHandler = menuHandler;
        this.userDataStorage = userDataStorage;
        this.linksTracker = linksTracker;
    }

    @Override
    public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (!MessagesApprovalUtils.isMsgExists(update.message())) {
            return Optional.empty();
        }

        var response = constructTemplateResponse(update, userData);
        setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        Locale userLocale = userData.getLocale();

        if (MessagesApprovalUtils.isCorrectResource(update.message())) {
            Link receivedLink = Link.constructLink(update.message().text().strip());
            linksTracker.addUserLink(userData.getUserID(), receivedLink);

            var response = new BaseRequest[] {new SendMessage(
                userData.getUserID(),
                answersProvider.resRegisteredSuccess(userLocale)
            ).replyToMessageId(update.message().messageId()).parseMode(ParseMode.Markdown)};
            return BotResponsesUtils.concatenate(response, menuHandler.constructTemplateResponse(update, userData));
        } else {
            return new BaseRequest[] {new SendMessage(
                userData.getUserID(),
                answersProvider.resTypingError(userLocale)
            ).replyToMessageId(update.message().messageId()).parseMode(ParseMode.Markdown)};
        }
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        if (responses.length > 1) {
            userDataStorage.setUserState(userData, BotState.MAIN_MENU);
        }
    }
}
