package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
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
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class ListHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final Keyboard keyboard;
    private final UserDataStorage userDataStorage;
    private final UserLinksTracker linksTracker;

    @Autowired
    public ListHandler(
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
    public BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        var userId = userData.getUserID();
        var userLocale = userData.getLocale();
        var links = linksTracker.getUserLinks(userId);

        if (links.isEmpty()) {
            return new SendMessage[] {new SendMessage(userId, answersProvider.noResYet(userLocale))};
        }

        return new BaseRequest[] {
            new SendMessage(
                userId,
                answersProvider.transitionList(userLocale)
            ).replyMarkup(keyboard.goBack(userLocale)),
            new SendMessage(
                userId, answersProvider.stateList(userLocale)
            ).replyMarkup(keyboard.userLinks(links))
        };
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        if (responses.length > 1) {
            userDataStorage.setUserState(userData, BotState.RES_LIST);
        } else {
            userDataStorage.setUserState(userData, BotState.MAIN_MENU);
        }
    }

    private boolean isInappropriateRequest(Update update, UserData userData) {
        return !MessagesApprovalUtils.equalsTextMessageContent(update.message(), "/list")
               || !userData.isRegistered();
    }
}
