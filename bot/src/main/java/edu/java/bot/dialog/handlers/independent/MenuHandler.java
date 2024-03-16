package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.data.UserDataStorage;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.dialog.lang.Keyboard;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final Keyboard keyboard;
    private final UserDataStorage userDataStorage;

    @Autowired
    public MenuHandler(
        @NotNull BotAnswersProvider answersProvider,
        @NotNull Keyboard keyboard,
        @NotNull UserDataStorage userDataStorage
    ) {
        this.answersProvider = answersProvider;
        this.keyboard = keyboard;
        this.userDataStorage = userDataStorage;
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
        var userLocale = userData.getLocale();
        return new SendMessage[] {new SendMessage(
            userData.getUserID(),
            answersProvider.mainMenu(userLocale)
        ).replyMarkup(keyboard.mainMenu(userLocale)).parseMode(ParseMode.Markdown)};
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        userDataStorage.setUserState(userData, BotState.MAIN_MENU);
    }

    private boolean isInappropriateRequest(Update update, UserData userData) {
        return !MessagesApprovalUtils.equalsTextMessageContent(update.message(), "/menu")
               || !userData.isRegistered();
    }
}
