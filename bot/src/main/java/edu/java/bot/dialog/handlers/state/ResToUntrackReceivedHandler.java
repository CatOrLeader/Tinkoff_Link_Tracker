package edu.java.bot.dialog.handlers.state;

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
import edu.java.bot.utils.BotResponsesUtils;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Locale;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class ResToUntrackReceivedHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UpdateHandler menuHandler;
    private final UserDataStorage userDataStorage;
    private final UserLinksTracker linksTracker;

    @Autowired
    public ResToUntrackReceivedHandler(
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
        if (MessagesApprovalUtils.isMsgExists(update.message())) {
            var maybeHandler = getAppropriateHandler(update.message().text(), userData);
            if (maybeHandler.isPresent()) {
                var handler = maybeHandler.get();
                var response = handler.constructTemplateResponse(update, userData);
                handler.setStateToLogicallyNext(response, userData);
                return Optional.ofNullable(response);
            }
        }

        if (!MessagesApprovalUtils.isCallbackQueryExists(update)) {
            return Optional.empty();
        }

        var callbackQuery = update.callbackQuery();
        var userId = userData.getUserID();

        int linkUrlStr = BotResponsesUtils.extractLinkCodeFromCallbackQuery(callbackQuery);
        linksTracker.removeUserLinkByCode(userId, linkUrlStr);

        var response = constructTemplateResponse(update, userData);
        setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        var innerResponse = new SendMessage[] {new SendMessage(
            userData.getUserID(),
            answersProvider.resUnregisterSuccess(userData.getLocale())
        ).parseMode(ParseMode.Markdown)};
        var outerResponse = menuHandler.constructTemplateResponse(update, userData);

        return BotResponsesUtils.concatenate(innerResponse, outerResponse);
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        userDataStorage.setUserState(userData, BotState.MAIN_MENU);
    }

    private Optional<UpdateHandler> getAppropriateHandler(String query, UserData userData) {
        Locale userLocale = userData.getLocale();
        if (query.contentEquals(answersProvider.goBackBtn(userLocale))) {
            return Optional.of(menuHandler);
        } else {
            return Optional.empty();
        }
    }
}
