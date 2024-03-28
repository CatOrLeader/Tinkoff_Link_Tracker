package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class HelpHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;

    @Override public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (isInappropriateRequest(update.message(), userData)) {
            return Optional.empty();
        }

        var response = constructTemplateResponse(update, userData);
        setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        return new SendMessage[] {
            new SendMessage(
                userData.getUserId(),
                answersProvider.commands(userData.getLocale())
            ).parseMode(ParseMode.Markdown)
        };
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        userData.setDialogState(BotState.MAIN_MENU);
    }

    private boolean isInappropriateRequest(Message message, UserData userData) {
        return !MessagesApprovalUtils.equalsTextMessageContent(message, "/help")
               || !userData.isRegistered();
    }
}
