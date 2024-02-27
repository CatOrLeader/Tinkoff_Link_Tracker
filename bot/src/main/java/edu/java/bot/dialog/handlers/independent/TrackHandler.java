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
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public final class TrackHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UserDataStorage userDataStorage;

    public TrackHandler(
        @NotNull BotAnswersProvider answersProvider,
        @NotNull UserDataStorage userDataStorage
    ) {
        this.answersProvider = answersProvider;
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
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        return new SendMessage[] {new SendMessage(
            userData.getUserID(),
            answersProvider.resRegisterWaiting(userData.getLocale())
        ).parseMode(ParseMode.Markdown)};
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        userDataStorage.setUserState(userData, BotState.RES_TRACK_WAITING);
    }

    private boolean isInappropriateRequest(Update update, UserData userData) {
        return !MessagesApprovalUtils.equalsTextMessageContent(update.message(), "/track")
               || !userData.isRegistered();
    }
}
