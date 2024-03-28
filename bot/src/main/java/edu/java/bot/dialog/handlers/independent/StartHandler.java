package edu.java.bot.dialog.handlers.independent;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import edu.java.bot.dialog.data.BotState;
import edu.java.bot.dialog.data.UserData;
import edu.java.bot.dialog.handlers.UpdateHandler;
import edu.java.bot.dialog.lang.BotAnswersProvider;
import edu.java.bot.utils.BotResponsesUtils;
import edu.java.bot.utils.MessagesApprovalUtils;
import java.util.Locale;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public final class StartHandler implements UpdateHandler {
    private final BotAnswersProvider answersProvider;
    private final UpdateHandler menuHandler;

    @Override public Optional<BaseRequest[]> handle(@NotNull Update update, @NotNull UserData userData) {
        if (isInappropriateRequest(update)) {
            return Optional.empty();
        }

        var response = constructTemplateResponse(update, userData);
        setStateToLogicallyNext(response, userData);

        return Optional.of(response);
    }

    @Override
    public @NotNull BaseRequest[] constructTemplateResponse(@NotNull Update update, @NotNull UserData userData) {
        var userId = userData.getUserId();
        Locale userLocale = userData.getLocale();

        if (userData.isRegistered()) {
            return new SendMessage[] {
                new SendMessage(userId, answersProvider.userAlreadyRegistered(userLocale))
                    .parseMode(ParseMode.Markdown)
            };
        } else {
            var innerResponse = new BaseRequest[] {
                new SendMessage(
                    userId,
                    answersProvider.userRegistered(userLocale)
                ).parseMode(ParseMode.Markdown),
                new SendMessage(
                    userId,
                    answersProvider.userRegisteredSuggestion(userLocale)
                ).parseMode(ParseMode.Markdown)
            };
            var outerResponse = menuHandler.constructTemplateResponse(update, userData);

            return BotResponsesUtils.concatenate(innerResponse, outerResponse);
        }
    }

    @Override
    public void setStateToLogicallyNext(@NotNull BaseRequest[] responses, @NotNull UserData userData) {
        userData.setDialogState(BotState.MAIN_MENU);
    }

    private boolean isInappropriateRequest(Update update) {
        return !MessagesApprovalUtils.equalsTextMessageContent(update.message(), "/start");
    }
}
