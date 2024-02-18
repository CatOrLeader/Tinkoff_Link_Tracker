package edu.java.bot.dialog.handlers.state;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public final class UninitializedHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;

    @Autowired
    public UninitializedHandler(@NotNull BotAnswersProvider answersProvider) {
        this.answersProvider = answersProvider;
    }

    @Override
    public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (!MessagesApprovalUtils.isMsgExists(update.message())) {
            return Optional.empty();
        }

        var response = constructTemplateResponse(update, userData);

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        return new SendMessage[] {new SendMessage(
            userData.getUserID(),
            answersProvider.uninitialized(userData.getLocale())
        ).parseMode(ParseMode.Markdown)};
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
    }
}
